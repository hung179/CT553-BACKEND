package com.ecommerce.studentmarket.student.address.exceptions;

public class AddressLimitExceededException extends AddressException {
    public AddressLimitExceededException() {
        super("Sinh viên chỉ được sở hữu tối đa 5 địa chỉ.");}
}
