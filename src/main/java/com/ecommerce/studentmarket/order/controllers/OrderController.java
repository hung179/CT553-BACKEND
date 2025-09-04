package com.ecommerce.studentmarket.order.controllers;

import com.ecommerce.studentmarket.order.dtos.request.OrderRequestDto;
import com.ecommerce.studentmarket.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("all/{mssv}")
    @PreAuthorize("hasRole('admin') or #mssv == authentication.name")
    public ResponseEntity<?> getAllOrderByMssv(
            @PathVariable String mssv,
            @RequestParam Integer page,
            @RequestParam Integer size){
        return ResponseEntity.ok(orderService.getAllOrderByMssv(mssv, page, size));
    }

    @GetMapping("/{maDH}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getOrderById(
            @PathVariable Long maDH){
        return ResponseEntity.ok(orderService.getOrderById(maDH));
    }

    @PostMapping("create/")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @PostMapping("create/pay")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> createAndPayTheOrder(
            @RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.payTheOrder(orderRequestDto));
    }
}
