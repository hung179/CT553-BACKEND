package com.ecommerce.studentmarket.order.controllers;

import com.ecommerce.studentmarket.order.services.SubOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subOrders")
public class SubOrderController {
    @Autowired
    private SubOrderService subOrderService;

    @PostMapping("xacNhan/{maDHC}")
    public ResponseEntity<?> changeOrderStateToXacNhan(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToXacNhan(maDHC));
    }

    @PostMapping("dangGiao/{maDHC}")
    public ResponseEntity<?> changeOrderStateToDangGiao(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToDangGiao(maDHC));
    }
    @PostMapping("daGiao/{maDHC}")
    public ResponseEntity<?> changeOrderStateToDaGiao(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToDaGiao(maDHC));
    }
    @PostMapping("daNhan/{maDHC}")
    public ResponseEntity<?> changeOrderStateToDaNhan(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToDaNhan(maDHC));
    }
    @PostMapping("daHuy/{maDHC}")
    public ResponseEntity<?> changeOrderStateToDaHuy(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToDaHuy(maDHC));
    }
    @PostMapping("daHoanTien/{maDHC}")
    public ResponseEntity<?> changeOrderStateToDaHoanTien(
            @PathVariable Long maDHC){
        return ResponseEntity.ok(subOrderService.changeOrderStateToDaHoanTien(maDHC));
    }
}
