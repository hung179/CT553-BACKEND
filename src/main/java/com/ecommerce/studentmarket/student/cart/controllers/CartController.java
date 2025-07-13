package com.ecommerce.studentmarket.student.cart.controllers;


import com.ecommerce.studentmarket.student.cart.dtos.CartItemDto;
import com.ecommerce.studentmarket.student.cart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/all/{mssv}")
    @PreAuthorize("#mssv == authentication.name")
    public ResponseEntity<?> getAllItems(
            @PathVariable String mssv,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(cartService.getCartByUserId(mssv));
    }
    @PostMapping("/addItem/{mssv}")
    @PreAuthorize("#mssv == authentication.name")
    public ResponseEntity<?> addItem(
            @PathVariable String mssv,
            @RequestBody CartItemDto cartItemDto
            ){
        return ResponseEntity.ok(cartService.addToCart(mssv, cartItemDto));
    }

    @PatchMapping("/updateItem/{mssv}")
    @PreAuthorize("#mssv == authentication.name")
    public ResponseEntity<?> updateItem(
            @PathVariable String mssv,
            @RequestBody CartItemDto cartItemDto
    ){
        return ResponseEntity.ok(cartService.updateCartItem(mssv, cartItemDto));
    }
}
