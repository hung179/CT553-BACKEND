package com.ecommerce.studentmarket.student.user.exceptions;

public class StudentAccountLockedException extends RuntimeException {
  public StudentAccountLockedException(String message) {
    super(message);
  }
}
