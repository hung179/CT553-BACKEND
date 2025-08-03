package com.ecommerce.studentmarket.common.paypal.controllers;

import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaypalController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String showPaymentForm() {
        return "payment-form";
    }

    private static final String SUCCESS_URL = "http://localhost:3000/success";
    private static final String CANCEL_URL = "http://localhost:3000/cancel";

    @PostMapping("/pay")
    public String processPayment(@RequestParam("amount") Double amount){
        try {
            Payment payment = paymentService.createPaymentWithPaypal(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "Mô tả",
                    CANCEL_URL,
                    SUCCESS_URL
            );
            for (Links link : payment.getLinks()){
                if (link.getRel().equals("approval_url")){
                    return "redirect:" + link.getHref();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Thành công";
    }

    @GetMapping("/success")
    public String successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerId") String payerId
    ){
        try{
            Payment payment = paymentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")){
                return SUCCESS_URL;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "http://localhost:3000/payment-failed";
    }
    @GetMapping("/cancel")
    public String cancelPay() {
        return "http://localhost:3000/payment-cancelled";
    }

}
