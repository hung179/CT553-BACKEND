package com.ecommerce.studentmarket.student.address.exceptions;



public class StudentNotFoundException extends AddressException {
    public StudentNotFoundException(String mssv) {
        super("Không tìm thấy sinh viên với mã số "+ mssv);
    }
}