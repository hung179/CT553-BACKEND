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

    @GetMapping("getAll")
    public ResponseEntity<?> getAllSubOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(subOrderService.getAllSubOrders(page, size));
    }

    @GetMapping("XacNhanById/{maGianHangDHC}")
    public  ResponseEntity<?> getSubOrdersByMaGianHangAndDaNhan(
            @PathVariable Long maGianHangDHC,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "8") Integer size
    ){
        return ResponseEntity.ok(subOrderService.getSubOrdersByMaGianHangAndDaNhan(maGianHangDHC, page, size));
    }

    @GetMapping("byId/{maGianHangDHC}")
    public  ResponseEntity<?> getSubOrdersByMaGianHang(
            @PathVariable Long maGianHangDHC,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "8") Integer size
            ){
        return ResponseEntity.ok(subOrderService.getSubOrdersByMaGianHang(maGianHangDHC, page, size));
    }

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

    @GetMapping("filter")
    public ResponseEntity<?> filterByTrangThai(
            @RequestParam String trangThai,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(subOrderService.getSubOrdersByTrangThai(trangThai, page, size));
    }

}
