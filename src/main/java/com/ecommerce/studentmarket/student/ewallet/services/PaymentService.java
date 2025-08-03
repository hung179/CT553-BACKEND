package com.ecommerce.studentmarket.student.ewallet.services;

import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import com.ecommerce.studentmarket.student.ewallet.dtos.MomoNotifyRequest;
import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.repositories.EwalletRepository;
import com.ecommerce.studentmarket.student.ewallet.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepo;
    private final EwalletRepository ewalletRepo;

    public String createMoMoPayment(Long amount, String mssv) {
        EwalletDomain wallet = ewalletRepo.findByStudent_Mssv(mssv);

        TransactionDomain transaction = new TransactionDomain();
        transaction.setSoTienGD(amount);
        transaction.setThoiGianGD(new Date());
        transaction.setTrangThaiGD(TrangThaiGiaoDich.CHUATHANHTOAN);
        transaction.setLoaiGD(LoaiGiaoDich.NAPTIEN);
        transaction.setWallet(wallet);

        transaction = transactionRepo.save(transaction);

        return MomoService.buildPaymentUrl(
                transaction.getMaGD().toString(),
                amount,
                "https://yourdomain.com/payments/return",
                "https://yourdomain.com/payments/notify"
        );
    }

    public boolean handleMoMoNotify(MomoNotifyRequest notify) {
        if (!MomoService.isSignatureValid(notify)) return false;

        Long orderId = Long.parseLong(notify.getOrderId());
        TransactionDomain transaction = transactionRepo.findById(orderId).orElseThrow();

        if ("0".equals(notify.getResultCode())) {
            transaction.setTrangThaiGD(TrangThaiGiaoDich.DATHANHTOAN);
            EwalletDomain wallet = transaction.getWallet();
            wallet.setSoDuVDT(wallet.getSoDuVDT() + transaction.getSoTienGD());
            ewalletRepo.save(wallet);
        } else {
            transaction.setTrangThaiGD(TrangThaiGiaoDich.CHUATHANHTOAN);
        }

        transactionRepo.save(transaction);
        return true;
    }
}
