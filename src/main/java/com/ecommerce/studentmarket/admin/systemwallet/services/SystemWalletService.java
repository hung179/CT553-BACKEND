package com.ecommerce.studentmarket.admin.systemwallet.services;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemTransactionDomain;
import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.admin.systemwallet.dtos.SystemTransactionRequestDto;
import com.ecommerce.studentmarket.admin.systemwallet.dtos.SystemTransactionResponseDto;
import com.ecommerce.studentmarket.admin.systemwallet.dtos.SystemWalletResponseDto;
import com.ecommerce.studentmarket.admin.systemwallet.repositories.SystemTransactionRepository;
import com.ecommerce.studentmarket.admin.systemwallet.repositories.SystemWalletRepository;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class SystemWalletService {

    @Autowired
    private SystemWalletRepository systemWalletRepository;

    @Autowired
    private SystemTransactionRepository systemTransactionRepository;

//    Lấy ví hệ thống
    public SystemWalletResponseDto getSystemWallet() {
        SystemWalletDomain systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc().orElseThrow(
                () -> new RuntimeException("Không tìm thấy ví hệ thống")
        );

        return convertToSystemWalletResponseDto(systemWallet);
    }
//Lấy giao dịch được lưu trong ví hệ thống
    public Page<SystemTransactionResponseDto> getPagedSystemTransactionsBySystemWallet(Long maVHT ,Pageable pageable) {

        Page<SystemTransactionDomain> page = systemTransactionRepository.findAllBySystemWallet_MaVHT(maVHT, pageable);

        return page.map(this::convertToSystemTransactionResponseDto);
    }
//Hàm lấy thông tin giao dịch nạp tiền của sinh viên
    public void handleSuccessfulPayment(SystemTransactionRequestDto systemTransactionRequestDto) {
        SystemWalletDomain systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc().orElseThrow(
                () -> new RuntimeException("Không tìm thấy ví hệ thống")
        );

        SystemTransactionDomain transactionDomain = convertToSystemTransactionDomain(systemTransactionRequestDto);

        transactionDomain.setSystemWallet(systemWallet);

        systemWallet.getTransactions().add(transactionDomain);

        systemWalletRepository.save(systemWallet);
    }

// Hàm chiết khấu thanh toán đơn hàng cho hệ thống
    public void handlePayTheOrder(BigDecimal amount, SystemTransactionRequestDto systemTransactionRequestDto){
        SystemWalletDomain systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc().orElseThrow(
                () -> new RuntimeException("Không tìm thấy ví hệ thống")
        );

        BigDecimal total = systemWallet.getSoDuVHT().add(amount);

        systemWallet.setSoDuVHT(total);

        SystemTransactionDomain transactionDomain = convertToSystemTransactionDomain(systemTransactionRequestDto);

        transactionDomain.setMaGDHT(null);
        transactionDomain.setSystemWallet(systemWallet);
        systemTransactionRepository.save(transactionDomain);

        systemWalletRepository.save(systemWallet);
    }

    private SystemTransactionDomain convertToSystemTransactionDomain(SystemTransactionRequestDto systemTransactionRequestDto){

        SystemTransactionDomain transactionDomain = new SystemTransactionDomain();

        Optional.ofNullable(systemTransactionRequestDto.getSoTienGDHT()).ifPresent(transactionDomain::setSoTienGDHT);
        Optional.ofNullable(systemTransactionRequestDto.getLoaiGDHT()).ifPresent(transactionDomain::setLoaiGDHT);
        Optional.ofNullable(systemTransactionRequestDto.getThoiGianGDHT()).ifPresent(transactionDomain::setThoiGianGDHT);
        Optional.ofNullable(systemTransactionRequestDto.getTrangThaiGDHT()).ifPresent(transactionDomain::setTrangThaiGDHT);
        transactionDomain.setIdDonHangGDHT(systemTransactionRequestDto.getIdDonHangGDHT());
        Optional.ofNullable(systemTransactionRequestDto.getIdGiaoDichHT()).ifPresent(transactionDomain::setIdGiaoDichHT);
        Optional.ofNullable(systemTransactionRequestDto.getChiTietGDHT()).ifPresent(transactionDomain::setChiTietGDHT);
        Optional.ofNullable(systemTransactionRequestDto.getMssvGDHT()).ifPresent(transactionDomain::setMssvGDHT);
        Optional.ofNullable(systemTransactionRequestDto.getHoTenSVGDHT()).ifPresent(transactionDomain::setHoTenSVGDHT);

        return transactionDomain;
    }

    private SystemWalletResponseDto convertToSystemWalletResponseDto(SystemWalletDomain systemWalletDomain){
        SystemWalletResponseDto responseDto = new SystemWalletResponseDto();

        Optional.ofNullable(systemWalletDomain.getMaVHT()).ifPresent(responseDto::setMaVHT);
        Optional.ofNullable(systemWalletDomain.getSoDuVHT()).ifPresent(responseDto::setSoDuVHT);

        return responseDto;
    }



    private SystemTransactionResponseDto convertToSystemTransactionResponseDto(SystemTransactionDomain systemTransactionDomain ){
        SystemTransactionResponseDto responseDto = new SystemTransactionResponseDto();

        Optional.ofNullable(systemTransactionDomain.getMaGDHT()).ifPresent(responseDto::setMaGD);
        Optional.ofNullable(systemTransactionDomain.getSoTienGDHT()).ifPresent(responseDto::setSoTienGD);
        Optional.ofNullable(systemTransactionDomain.getLoaiGDHT()).ifPresent(responseDto::setLoaiGD);
        Optional.ofNullable(systemTransactionDomain.getThoiGianGDHT()).ifPresent(responseDto::setThoiGianGD);
        Optional.ofNullable(systemTransactionDomain.getTrangThaiGDHT()).ifPresent(responseDto::setTrangThaiGD);
        responseDto.setIdDonHangGD(systemTransactionDomain.getIdDonHangGDHT());
        Optional.ofNullable(systemTransactionDomain.getChiTietGDHT()).ifPresent(responseDto::setChiTietGD);

        return responseDto;

    }

//    Thu phí rút tiền và lưu giao dịch hệ thống
    public void handleWithdrawFee(BigDecimal phiRut, SystemTransactionRequestDto feeTransaction) {
        // 1. Lấy ví hệ thống
        SystemWalletDomain systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví hệ thống"));

        // 2. Cộng phí rút vào số dư ví hệ thống
        BigDecimal newBalance = systemWallet.getSoDuVHT().add(phiRut);
        systemWallet.setSoDuVHT(newBalance);

        // 3. Tạo bản ghi giao dịch
        SystemTransactionDomain transactionDomain = convertToSystemTransactionDomain(feeTransaction);
        transactionDomain.setSystemWallet(systemWallet);

        // 4. Lưu giao dịch vào repository
        systemTransactionRepository.save(transactionDomain);

        // 5. Lưu lại ví hệ thống
        systemWalletRepository.save(systemWallet);
    }


    public void updateSystemTransactionByBatchId(String payoutBatchId, TrangThaiGiaoDich trangThaiGiaoDich) {

        SystemTransactionDomain systemTransactionDomain = systemTransactionRepository.findByIdGiaoDichHT(payoutBatchId);

        systemTransactionDomain.setTrangThaiGDHT(trangThaiGiaoDich);

        systemTransactionRepository.save(systemTransactionDomain);
    }
}
