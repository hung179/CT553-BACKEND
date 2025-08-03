package com.ecommerce.studentmarket.admin.user.exceptions;

public class AdminNotFoundException extends AdminException {
    public AdminNotFoundException(String ma) {
        super("Cán bộ với số " + ma + " không tìm thấy.");
    }
}