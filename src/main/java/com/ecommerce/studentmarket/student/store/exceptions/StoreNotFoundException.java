package com.ecommerce.studentmarket.student.user.exceptions;


public class StudentNotFoundException extends StudentException {
    public StudentNotFoundException(String mssv) {
        super("Không tìm thấy sinh viên với mã số "+ mssv);
    }
}