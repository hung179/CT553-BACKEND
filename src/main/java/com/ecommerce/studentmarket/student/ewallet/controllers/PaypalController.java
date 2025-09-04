package com.ecommerce.studentmarket.student.ewallet.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import com.ecommerce.studentmarket.student.ewallet.services.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @PreAuthorize("hasRole('student')")
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
    @PreAuthorize("#mssv == authentication.name")
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
        }
        return ResponseEntity.badRequest().body("error");
    }

    @PostMapping("/withdraw/{mssv}")
    @PreAuthorize("#mssv == authentication.name")
    public ResponseEntity<?> withdraw(
            @PathVariable String mssv,
            @RequestParam String email,
            @RequestParam String amount,
            @RequestParam("amountVND") BigDecimal amountVND,
            @RequestParam(defaultValue = "USD") String currency) {
        try {
            // Gửi yêu cầu rút tiền và nhận batchId
            String batchId = paypalService.sendPayout(email, amount, currency, mssv, amountVND);
            paymentService.withdrawFromEwallet(
                    mssv,
                    amountVND,
                    email,
                    batchId
            );
            return ResponseEntity.ok(new ApiResponse("Yêu cầu rút tiền thành công, vui lòng chờ xử lý", true, ApiResponseType.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse("Yêu cầu rút tiền thất bại", true, ApiResponseType.NOTFOUND));
        }
    }

    @PostMapping("/webhook")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<?> handleWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader Map<String, String> headers) {
        try {
            paypalService.processWebhook(payload, headers);
            return ResponseEntity.ok(Map.of("status", "Webhook processed successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Webhook processing failed",
                    "details", e.getMessage()
            ));
        }
    }
}
