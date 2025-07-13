package com.ecommerce.studentmarket.product.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.product.exceptions.ProductAlreadyDeletedException;
import com.ecommerce.studentmarket.product.exceptions.ProductAlreadyExistsException;
import com.ecommerce.studentmarket.product.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductContrllerAdvice {
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadyExists(ProductAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(ProductAlreadyDeletedException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadDeleted (ProductAlreadyDeletedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false));
    }
}
