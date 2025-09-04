package com.ecommerce.studentmarket.admin.systemwallet.exceptions;

public class SystemWalletNotFoundException extends SystemWalletException {
    public SystemWalletNotFoundException(){
        super("Không tìm được ví hệ thống");
    }
}
