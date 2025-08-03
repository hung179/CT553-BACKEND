package com.ecommerce.studentmarket.student.store.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.store.exceptions.StoreAlreadyExistsException;
import com.ecommerce.studentmarket.student.store.exceptions.StoreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class StoreControllerAdvice {

    @ExceptionHandler(StoreAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleStoreAlreadyExists(StoreAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStoreNotFound( StoreNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }
}
