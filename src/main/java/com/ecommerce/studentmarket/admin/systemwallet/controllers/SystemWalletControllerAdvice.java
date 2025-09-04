package com.ecommerce.studentmarket.admin.systemwallet.controllers;

import com.ecommerce.studentmarket.admin.systemwallet.exceptions.SystemWalletNotFoundException;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SystemWalletControllerAdvice {

    @ExceptionHandler(SystemWalletNotFoundException.class)
    public ResponseEntity<ApiResponse> handleSystemWalletNotFound(SystemWalletNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

}
