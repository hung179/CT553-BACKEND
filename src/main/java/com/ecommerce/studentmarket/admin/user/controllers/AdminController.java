package com.ecommerce.studentmarket.admin.user.controllers;


import com.ecommerce.studentmarket.admin.user.dtos.AdminRequestDto;
import com.ecommerce.studentmarket.admin.user.services.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminRequestDto adminData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminData));
    }

//    Hàm trả về thông tin của chính bản thân qua token
    @GetMapping("/myInfo")
    @PostAuthorize("returnObject.body.mscb == authentication.name")
    public ResponseEntity<?> getMyInfo() {
        return ResponseEntity.ok(adminService.getMyInfo());
    }

    @GetMapping("/{mscb}")
    @PreAuthorize("#mscb == authentication.name or authentication.name == '00001'")
    public ResponseEntity<?> getAdmin(@PathVariable String mscb){
        return ResponseEntity.ok(adminService.getAdminById(mscb));
    }
    @PatchMapping("/{mscb}")
    @PreAuthorize("#mscb == authentication.name or authentication.name == '00001'")
    public ResponseEntity<?> updateAdmin(@PathVariable String mscb, @RequestBody AdminRequestDto adminRequestDto){
            return ResponseEntity.ok(adminService.updateAdminById(mscb, adminRequestDto));
    }

//    Có thể sẽ bỏ
    @DeleteMapping("/{mscb}")
    @PreAuthorize("#mscb == authentication.name or authentication.name == '00001'")
    public ResponseEntity<?> deleteAdmin(@PathVariable String mscb){
            return ResponseEntity.ok(adminService.deleteAdminById(mscb));
    }
}