package com.ecommerce.studentmarket.student.cart.services;

import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;
import com.ecommerce.studentmarket.student.cart.dtos.CartItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartItemService {

    public CartItemDomain createNewCartItem(CartDomain cartDomain, CartItemDto cartItemDto){
        CartItemIdDomain itemId = new CartItemIdDomain(cartDomain.getIdGioHang(), cartItemDto.getMaSP());

        CartItemDomain newItem = new CartItemDomain();
        Optional.of(itemId).ifPresent(newItem::setId);
        Optional.ofNullable(cartItemDto.getSoLuong()).ifPresent(newItem::setSoLuong);
        Optional.ofNullable(cartItemDto.getMaSP()).ifPresent(newItem::setMaSP);
        Optional.of(cartDomain).ifPresent(newItem::setCartDomain);

        return  newItem;
    }
}
