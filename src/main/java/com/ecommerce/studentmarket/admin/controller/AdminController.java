package com.ecommerce.studentmarket.admin.controller;


import com.ecommerce.studentmarket.admin.dto.AdminDto;
import com.ecommerce.studentmarket.admin.exception.AdminNotFoundException;
import com.ecommerce.studentmarket.admin.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping()
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminDto adminData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminData));
    }

    @GetMapping("/{mscb}")
    public ResponseEntity<?> getAdmin(@PathVariable String mscb){
        return ResponseEntity.ok(adminService.getAdminById(mscb));
    }
    @PatchMapping("/{mscb}")
    public ResponseEntity<?> updateAdmin(@PathVariable String mscb, @RequestBody AdminDto adminDto){
            return ResponseEntity.ok(adminService.updateAdminById(mscb, adminDto));
    }

    @DeleteMapping("/{mscb}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String mscb){
            return ResponseEntity.ok(adminService.deleteAdminById(mscb));
    }
}