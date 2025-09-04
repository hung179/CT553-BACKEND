package com.ecommerce.studentmarket.student.address.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.address.exceptions.AddressAlreadyExistsException;
import com.ecommerce.studentmarket.student.address.exceptions.AddressLimitExceededException;
import com.ecommerce.studentmarket.student.address.exceptions.AddressNotFoundException;
import com.ecommerce.studentmarket.student.address.exceptions.StudentNotFoundException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemAlreadyExistsException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemNotFoundException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemProductOutOfStockException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AddressControllerAdvice {
    @ExceptionHandler(AddressAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleAddressAlreadyExists(AddressAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAddressNotFound(AddressNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

    @ExceptionHandler(AddressLimitExceededException.class)
    public ResponseEntity<ApiResponse> handleAddressLimitExceeded(AddressLimitExceededException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, ApiResponseType.INVALID));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStudentNotFound(StudentNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }
}
