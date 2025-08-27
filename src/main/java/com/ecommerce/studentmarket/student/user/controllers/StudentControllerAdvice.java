package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.common.authencation.exceptions.UnauthenticatedException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAccountLockedException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudentControllerAdvice {
    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleStudentAlreadyExists( StudentAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.EXISTS));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStudentNotFound( StudentNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false, ApiResponseType.NOTFOUND));
    }

    @ExceptionHandler(StudentAccountLockedException.class)
    public ResponseEntity<ApiResponse> handleStudentAccountLockedException( StudentAccountLockedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.ACCOUNTLOCKED));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUnauthenticatedException( UnauthenticatedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false, ApiResponseType.UNAUTHENTICATED));
    }

}
