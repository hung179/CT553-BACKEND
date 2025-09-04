package com.ecommerce.studentmarket.student.address.exceptions;

public class AddressAlreadyExistsException extends AddressException {
    public AddressAlreadyExistsException(Long maDC) {
        super("Địa chỉ với mã "+ maDC + " đã tồn tại.");}
}
