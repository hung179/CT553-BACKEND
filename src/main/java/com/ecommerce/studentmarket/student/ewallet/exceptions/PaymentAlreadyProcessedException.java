package com.ecommerce.studentmarket.student.ewallet.exceptions;

public class PaymentAlreadyProcessedException extends PaymentException {
    public PaymentAlreadyProcessedException() {super("Giao dịch này đã được xử lý");}
}
