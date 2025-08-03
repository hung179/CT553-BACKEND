package com.ecommerce.studentmarket.order.controllers;

import com.ecommerce.studentmarket.order.dtos.request.OrderRequestDto;
import com.ecommerce.studentmarket.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("all/{mssv}")
    public ResponseEntity<?> getAllOrderByMssv(
            @PathVariable String mssv,
            @RequestParam Integer page,
            @RequestParam Integer size){
        return ResponseEntity.ok(orderService.getAllOrderByMssv(mssv, page, size));
    }

    @GetMapping("/{maDH}")
    public ResponseEntity<?> getOrderById(
            @PathVariable Long maDH){
        return ResponseEntity.ok(orderService.getOrderById(maDH));
    }

    @PostMapping("create/")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @PostMapping("create/pay")
    public ResponseEntity<?> createAndPayTheOrder(
            @RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.payTheOrder(orderRequestDto));
    }

}
