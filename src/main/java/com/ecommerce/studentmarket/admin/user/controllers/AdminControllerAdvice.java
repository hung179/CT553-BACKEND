package com.ecommerce.studentmarket.admin.controllers;

import com.ecommerce.studentmarket.admin.exceptions.AdminAlreadyExistsException;
import com.ecommerce.studentmarket.admin.exceptions.AdminNotFoundException;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdminControllerAdvice {
    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleAdminAlreadyExists(AdminAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAdminNotFound(AdminNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

}
