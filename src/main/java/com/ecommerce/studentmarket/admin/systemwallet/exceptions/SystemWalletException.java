package com.ecommerce.studentmarket.admin.systemwallet.exceptions;

public class SystemWalletException extends RuntimeException{

    public SystemWalletException(String message){
        super(message);
    }

    public SystemWalletException(String message, Throwable cause){
        super(message, cause);
    }
}
