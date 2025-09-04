package com.ecommerce.studentmarket.order.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.order.exceptions.suborder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubOrderControllerAdvice {

    @ExceptionHandler(SubOrderNotFoundException.class)
    public ResponseEntity<ApiResponse> handleSubOrderNotFound(SubOrderNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

    @ExceptionHandler(SubOrderAlreadyProcessedException.class)
    public ResponseEntity<ApiResponse> handleSubOrderAlreadyProcessed(SubOrderAlreadyProcessedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.LIMITED));
    }

    @ExceptionHandler(SubOrderInvalidStateException.class)
    public ResponseEntity<ApiResponse> handleSubOrderInvalidState(SubOrderInvalidStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, ApiResponseType.INVALID));
    }

    @ExceptionHandler(SubOrderNotPendingApprovalException.class)
    public ResponseEntity<ApiResponse> handleSubOrderNotPendingApproval(SubOrderNotPendingApprovalException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, ApiResponseType.INVALID));
    }

    @ExceptionHandler(SubOrderStoreNotFoundException.class)
    public ResponseEntity<ApiResponse> handleSubOrderStoreNotFound(SubOrderStoreNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }


}
