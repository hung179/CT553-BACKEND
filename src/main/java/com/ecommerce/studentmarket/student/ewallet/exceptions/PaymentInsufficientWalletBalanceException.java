package com.ecommerce.studentmarket.student.ewallet.exceptions;

public class PaymentInsufficientWalletBalanceException extends PaymentException {
    public PaymentInsufficientWalletBalanceException() {super("Không đủ số dư trong ví, cần nạp thêm");}
}
