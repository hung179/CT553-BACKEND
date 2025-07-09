package com.ecommerce.studentmarket.admin.controller;

import com.ecommerce.studentmarket.admin.exception.AdminAlreadyExistsException;
import com.ecommerce.studentmarket.admin.exception.AdminNotFoundException;
import com.ecommerce.studentmarket.common.ApiConfig.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdminControllerAdvice {
    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleAdminAlreadyExists(AdminAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAdminNotFound(AdminNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
    }

}
