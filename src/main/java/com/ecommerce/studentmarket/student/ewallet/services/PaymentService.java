package com.ecommerce.studentmarket.student.ewallet.services;


import com.ecommerce.studentmarket.admin.systemwallet.dtos.SystemTransactionRequestDto;
import com.ecommerce.studentmarket.admin.systemwallet.services.SystemWalletService;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.common.exchangerate.ExchangeRateService;
import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import com.ecommerce.studentmarket.student.ewallet.dtos.EwalletResponseDto;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionRequestDto;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionResponseDto;
import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.repositories.EwalletRepository;
import com.ecommerce.studentmarket.student.ewallet.repositories.TransactionRepository;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private EwalletRepository ewalletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private SystemWalletService systemWalletService;

//    Nạp tiền
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> handleSuccessfulPayment(Payment payment, String mssv, BigDecimal amountVND){
        EwalletDomain ewalletDomain = ewalletRepository.findByStudent_Mssv(mssv);

        Transaction transaction = payment.getTransactions().getFirst();

        BigDecimal amount = new BigDecimal(transaction.getAmount().getTotal());

        amount = exchangeRateService.convertUsdToVnd(amount);

        BigDecimal totalCoin = ewalletDomain.getSoDuVDT().add(amountVND);

        ewalletDomain.setSoDuVDT(totalCoin);
        TransactionDomain transactionDomain = getTransactionDomain(transaction, ewalletDomain, totalCoin,payment.getId());

        List<TransactionDomain> currentTransaction = ewalletDomain.getTransactions();

        currentTransaction.add(transactionDomain);

        ewalletDomain.setTransactions(currentTransaction);

        ewalletRepository.save(ewalletDomain);

        TransactionDomain newTransaction = transactionRepository.findByIdGiaoDich(transactionDomain.getIdGiaoDich());

        SystemTransactionRequestDto systemTransactionRequestDto = convertToSystemTransactionRequestDto(newTransaction, ewalletDomain.getStudent().getMssv(), ewalletDomain.getStudent().getHoTen());

        systemWalletService.handleSuccessfulPayment(systemTransactionRequestDto);


        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("paymentId", payment.getId());
        response.put("mssv", mssv);
        response.put("convertedAmount", totalCoin);
        response.put("timestamp", transactionDomain.getThoiGianGD());
        response.put("newBalance", totalCoin);
        return response;
    }

    private TransactionDomain getTransactionDomain(Transaction transaction, EwalletDomain ewalletDomain, BigDecimal totalCoinInVnd ,String paymentId) {
        RelatedResources resource = transaction.getRelatedResources().getFirst();
        Sale sale = resource.getSale();

        String createTimeStr = sale.getCreateTime();
        Instant instant = Instant.parse(createTimeStr);
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        LocalDateTime thoiGianGD = LocalDateTime.ofInstant(instant, zoneId);

        Boolean exists = transactionRepository.existsByIdGiaoDich(paymentId);
        if (exists){
            throw new RuntimeException("Giao dịch này đã được xử lý");
        }

        TransactionDomain transactionDomain = new TransactionDomain();

        transactionDomain.setIdGiaoDich(paymentId);
        transactionDomain.setSoTienGD(totalCoinInVnd);
        transactionDomain.setThoiGianGD(thoiGianGD);
        transactionDomain.setLoaiGD(LoaiGiaoDich.NAPTIEN);
        transactionDomain.setTrangThaiGD(TrangThaiGiaoDich.DATHANHTOAN);
        transactionDomain.setChiTietGD(transaction.getDescription());
        transactionDomain.setWallet(ewalletDomain);
        return transactionDomain;
    }

//    Thanh toán đơn hàng
@Transactional(rollbackFor = Exception.class)
public ApiResponse handlePayTheOrder(String mssv, TransactionRequestDto transactionRequestDto) {

    EwalletDomain ewalletDomain = ewalletRepository.findByStudent_Mssv(mssv);

    BigDecimal totalCoin = ewalletDomain.getSoDuVDT();

    // Lấy số tiền giao dịch
    BigDecimal soTienGiaoDich = transactionRequestDto.getSoTienGD();

    // Tính chiết khấu 10%
    BigDecimal chietKhau = soTienGiaoDich.multiply(BigDecimal.valueOf(0.10));

    // Tổng tiền cần trừ = tiền hàng + chiết khấu
    BigDecimal tongTienCanTru = soTienGiaoDich.add(chietKhau);

    if (totalCoin.compareTo(tongTienCanTru) < 0) {
        throw new RuntimeException("Không đủ số dư ví, cần nạp thêm");
    }

    // Trừ tiền khỏi ví người mua
    totalCoin = totalCoin.subtract(tongTienCanTru);
    ewalletDomain.setSoDuVDT(totalCoin);

    // Tạo giao dịch
    TransactionDomain transactionDomain = new TransactionDomain();
    transactionDomain.setSoTienGD(soTienGiaoDich);
    transactionDomain.setLoaiGD(LoaiGiaoDich.THANHTOAN);
    transactionDomain.setThoiGianGD(LocalDateTime.now());
    transactionDomain.setTrangThaiGD(TrangThaiGiaoDich.DATHANHTOAN);
    transactionDomain.setIdDonHangGD(transactionRequestDto.getIdDonHangGD());
    transactionDomain.setChiTietGD(transactionRequestDto.getChiTietGD());
    transactionDomain.setWallet(ewalletDomain);

    ewalletDomain.getTransactions().add(transactionDomain);
    ewalletRepository.save(ewalletDomain);

    // Gửi tiền chiết khấu về ví hệ thống
    TransactionDomain newTransaction = transactionRepository.findByIdGiaoDich(transactionDomain.getIdGiaoDich());

    SystemTransactionRequestDto systemTransactionRequestDto = convertToSystemTransactionRequestDto(
            newTransaction, ewalletDomain.getStudent().getMssv(), ewalletDomain.getStudent().getHoTen());

    systemWalletService.handlePayTheOrder(chietKhau, systemTransactionRequestDto);

    return new ApiResponse("Thanh toán thành công", true, ApiResponseType.SUCCESS);
}


    //    Xác nhận chuyển hàng và nhận tiền cho người bán
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse receivePaymentForSeller(String mssv, TransactionRequestDto transactionRequestDto) {

        EwalletDomain ewalletDomain = ewalletRepository.findByStudent_Mssv(mssv);

        BigDecimal totalCoin = ewalletDomain.getSoDuVDT();

        // Số tiền người bán nhận là số tiền gốc đơn hàng (không trừ chiết khấu)
        BigDecimal soTienGiaoDich = transactionRequestDto.getSoTienGD();

        // Cộng tiền vào ví người bán
        totalCoin = totalCoin.add(soTienGiaoDich);
        ewalletDomain.setSoDuVDT(totalCoin);

        // Ghi lại giao dịch nhận tiền
        TransactionDomain transactionDomain = new TransactionDomain();
        transactionDomain.setSoTienGD(soTienGiaoDich);
        transactionDomain.setLoaiGD(LoaiGiaoDich.THANHTOAN);
        transactionDomain.setThoiGianGD(LocalDateTime.now());
        transactionDomain.setTrangThaiGD(TrangThaiGiaoDich.DATHANHTOAN);
        transactionDomain.setIdDonHangGD(transactionRequestDto.getIdDonHangGD());
        transactionDomain.setChiTietGD("Nhận tiền từ đơn hàng #" + transactionRequestDto.getIdDonHangGD());
        transactionDomain.setWallet(ewalletDomain);

        ewalletDomain.getTransactions().add(transactionDomain);
        ewalletRepository.save(ewalletDomain);

        return new ApiResponse("Người bán đã nhận tiền thành công", true, ApiResponseType.SUCCESS);
    }


    public EwalletResponseDto getEwalletInfo(String mssv) {

        EwalletDomain ewalletDomain = ewalletRepository.findByStudent_Mssv(mssv);

        return convertToEwalletResponseDto(ewalletDomain);

    }

    public Page<TransactionResponseDto> getPagedTransactionsByEwallet(Long maVDT, Pageable pageable) {

        Page<TransactionDomain> page = transactionRepository.findAllByWallet_MaVDT(maVDT, pageable);

        return page.map(this::convertToTransactionResponseDto);
    }

    private EwalletResponseDto convertToEwalletResponseDto(EwalletDomain ewalletDomain){
        EwalletResponseDto responseDto = new EwalletResponseDto();

        Optional.ofNullable(ewalletDomain.getMaVDT()).ifPresent(responseDto::setMaVDT);
        Optional.ofNullable(ewalletDomain.getSoDuVDT()).ifPresent(responseDto::setSoDuVDT);

        return responseDto;
    }

    private TransactionResponseDto convertToTransactionResponseDto(TransactionDomain transactionDomain ){
        TransactionResponseDto responseDto = new TransactionResponseDto();

        Optional.ofNullable(transactionDomain.getMaGD()).ifPresent(responseDto::setMaGD);
        Optional.ofNullable(transactionDomain.getSoTienGD()).ifPresent(responseDto::setSoTienGD);
        Optional.ofNullable(transactionDomain.getLoaiGD()).ifPresent(responseDto::setLoaiGD);
        Optional.ofNullable(transactionDomain.getThoiGianGD()).ifPresent(responseDto::setThoiGianGD);
        Optional.ofNullable(transactionDomain.getTrangThaiGD()).ifPresent(responseDto::setTrangThaiGD);
        responseDto.setIdDonHangGD(transactionDomain.getIdDonHangGD());
        Optional.ofNullable(transactionDomain.getChiTietGD()).ifPresent(responseDto::setChiTietGD);

        return responseDto;
    }

    private SystemTransactionRequestDto convertToSystemTransactionRequestDto(TransactionDomain transactionDomain, String mssv, String hoTen ){

        SystemTransactionRequestDto transactionRequestDto = new SystemTransactionRequestDto();

        Optional.ofNullable(transactionDomain.getMaGD()).ifPresent(transactionRequestDto::setMaGDHT);
        Optional.ofNullable(transactionDomain.getSoTienGD()).ifPresent(transactionRequestDto::setSoTienGDHT);
        Optional.ofNullable(transactionDomain.getLoaiGD()).ifPresent(transactionRequestDto::setLoaiGDHT);
        Optional.ofNullable(transactionDomain.getThoiGianGD()).ifPresent(transactionRequestDto::setThoiGianGDHT);
        Optional.ofNullable(transactionDomain.getTrangThaiGD()).ifPresent(transactionRequestDto::setTrangThaiGDHT);
        transactionRequestDto.setIdDonHangGDHT(transactionDomain.getIdDonHangGD());
        Optional.ofNullable(transactionDomain.getChiTietGD()).ifPresent(transactionRequestDto::setChiTietGDHT);
        Optional.ofNullable(mssv).ifPresent(transactionRequestDto::setMssvGDHT);
        Optional.ofNullable(hoTen).ifPresent(transactionRequestDto::setHoTenSVGDHT);

        return transactionRequestDto;

    }

}
