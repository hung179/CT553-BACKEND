package com.ecommerce.studentmarket.student.user.exceptions;

public class StudentAlreadyExistsException extends StudentException {
        public StudentAlreadyExistsException(String so) {
            super("Sinh viên với số "+ so + " đã tồn tại.");
        }
}
