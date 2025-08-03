package com.ecommerce.studentmarket.product.item.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.product.item.exceptions.ProductAlreadyDeletedException;
import com.ecommerce.studentmarket.product.item.exceptions.ProductAlreadyExistsException;
import com.ecommerce.studentmarket.product.item.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductContrllerAdvice {
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadyExists(ProductAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

    @ExceptionHandler(ProductAlreadyDeletedException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadDeleted (ProductAlreadyDeletedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.DELETED));
    }
}
