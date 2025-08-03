package com.ecommerce.studentmarket.student.cart.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemAlreadyExistsException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemNotFoundException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemProductOutOfStockException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CartControllerAdvice {
    @ExceptionHandler(CartItemAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleCartItemAlreadyExists(CartItemAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(CartItemUnavailableException.class)
    public ResponseEntity<ApiResponse> handleCartItemUnavailable(CartItemUnavailableException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.HIDED));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCartItemNotFound(CartItemNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

    @ExceptionHandler(CartItemProductOutOfStockException.class)
    public ResponseEntity<ApiResponse> handleCartItemProductOutOfStock(CartItemProductOutOfStockException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, ApiResponseType.LIMITED));
    }
}
