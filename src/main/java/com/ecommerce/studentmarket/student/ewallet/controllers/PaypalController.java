package com.ecommerce.studentmarket.student.ewallet.controllers;

import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import com.ecommerce.studentmarket.student.ewallet.services.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("paypal")
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private PaymentService paymentService;

    private static String SUCCESS_URL;
    private static String CANCEL_URL;
    private static BigDecimal amountVND;

    @PostMapping("/pay")
    public String processPayment(@RequestParam("amount") BigDecimal amount,
                                 @RequestParam("description") String description,
                                 @RequestParam("amountVND") BigDecimal amountVND,
                                 @RequestParam("SUCCESS_URL") String SUCCESS_URL,
                                 @RequestParam("CANCEL_URL") String CANCEL_URL
                                 ) {
        try {
            Payment payment = paypalService.createPaymentWithPaypal(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    description,
                    CANCEL_URL,
                    SUCCESS_URL
            );
            PaypalController.SUCCESS_URL = SUCCESS_URL;
            PaypalController.amountVND = amountVND;
            for (Links link : payment.getLinks()){
                if (link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Thành công";
    }

    @GetMapping("/success/{mssv}")
    public ResponseEntity<?> successPay(
            @PathVariable String mssv,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId
    ){
        try{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")){
                Map<String, Object> result = paymentService.handleSuccessfulPayment(payment, mssv, amountVND);
                return ResponseEntity.ok(result);
            }
        }catch (Exception e) {
            e.printStackTrace();
//            Vẫn còn vấn đề tiền chung hệ thống và khấu trừ tiền
        }
        return ResponseEntity.badRequest().body("error");
    }
}
