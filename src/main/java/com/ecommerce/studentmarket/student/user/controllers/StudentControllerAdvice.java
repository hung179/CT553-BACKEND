package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudentControllerAdvice
{
    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleStudentAlreadyExists( StudentAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStudentNotFound( StudentNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
    }}
