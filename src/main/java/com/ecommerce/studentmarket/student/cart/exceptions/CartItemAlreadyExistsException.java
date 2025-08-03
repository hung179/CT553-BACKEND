package com.ecommerce.studentmarket.student.cart.exceptions;

public class CartItemAlreadyExistsException extends CartException{
    public CartItemAlreadyExistsException (String tenSP) {super("Sản phẩm với mã "+ tenSP + " đã tồn tại trong giỏ hàng");}
}
