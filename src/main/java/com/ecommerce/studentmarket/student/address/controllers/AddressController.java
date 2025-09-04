package com.ecommerce.studentmarket.student.address.controllers;

import com.ecommerce.studentmarket.student.address.dtos.AddressRequestDto;
import com.ecommerce.studentmarket.student.address.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{maDC}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getAddressById(
            @PathVariable Long maDC
    ){
        return ResponseEntity.ok(addressService.getAddressById(maDC));
    }

    @GetMapping("/all/{mssv}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getAddressesByMssv(
            @PathVariable String mssv,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ){
        return ResponseEntity.ok(addressService.getAllAddress(mssv, page, size));
    }

    @PostMapping("/create/{mssv}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> createNewAddress(
            @PathVariable String mssv,
            @RequestBody AddressRequestDto dto
            ){
        return ResponseEntity.ok(addressService.createAddress(mssv, dto));
    }

    @PatchMapping(value = "/update/{mssv}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin') or #mssv == authentication.name")
    public ResponseEntity<?> patchAddress(
            @PathVariable String mssv,
            @RequestParam("maDC") Long maDC,
            @RequestPart("dto") AddressRequestDto dto
    ){
        return ResponseEntity.ok(addressService.patchAddress(mssv, maDC, dto));
    }

    @DeleteMapping("/delete/{mssv}")
    @PreAuthorize("hasRole('admin') or #mssv == authentication.name")
    public ResponseEntity<?> deleteAddress(
            @PathVariable String mssv,
            @RequestParam Long maDC
    ){
        return ResponseEntity.ok(addressService.deleteAddress(mssv, maDC));
    }
}
