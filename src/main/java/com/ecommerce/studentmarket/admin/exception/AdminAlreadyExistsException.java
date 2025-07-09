package com.ecommerce.studentmarket.admin.exception;

public class AdminAlreadyExistsException extends AdminException {
    public AdminAlreadyExistsException(String ma) {
        super("Cán bộ với mã " + ma + " đã tồn tại.");
    }
}
