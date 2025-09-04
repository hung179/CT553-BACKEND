package com.ecommerce.studentmarket.order.exceptions.suborder;

public class SubOrderException extends RuntimeException{

    public SubOrderException(String message){
        super(message);
    }

    public SubOrderException(String message, Throwable cause){
        super(message, cause);
    }
}
