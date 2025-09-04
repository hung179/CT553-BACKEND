package com.ecommerce.studentmarket.student.ewallet.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.ewallet.exceptions.PaymentAlreadyProcessedException;
import com.ecommerce.studentmarket.student.ewallet.exceptions.PaymentInsufficientWalletBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentControllerAdvice {

    @ExceptionHandler(PaymentAlreadyProcessedException.class)
    public ResponseEntity<ApiResponse> handlePaymentAlreadyProcessed(PaymentAlreadyProcessedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(PaymentInsufficientWalletBalanceException.class)
    public ResponseEntity<ApiResponse> handlePaymentInsufficientWalletBalance(PaymentInsufficientWalletBalanceException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, ApiResponseType.INVALID));
    }

}
