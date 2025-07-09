package com.ecommerce.studentmarket.student.cart.exceptions;

public class CartItemAlreadyExistsException extends CartException{
    public CartItemAlreadyExistsException (Long maSP) {super("Sản phẩm với mã "+ maSP + " đã tồn tại trong giỏ hàng");}
}
