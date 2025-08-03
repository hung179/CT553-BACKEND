package com.ecommerce.studentmarket.product.item.controllers;


import com.ecommerce.studentmarket.product.item.dtos.ProductRequestDto;
import com.ecommerce.studentmarket.product.item.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all/{maGHDT}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getAllProduct(
            @PathVariable Long maGHDT,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(productService.getAllProduct(maGHDT, page, size));
    }

    @GetMapping("/{maSP}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getProductById(
            @PathVariable Long maSP
    ){
        return ResponseEntity.ok(productService.getProductResponetById(maSP));

    }

    @GetMapping("/search/{maGHDT}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> searchProductByName(
            @PathVariable Long maGHDT,
            @RequestParam String tenSP,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        System.out.println(tenSP +" "+ page +" "+size);
        return ResponseEntity.ok(productService.searchProductByName(maGHDT, tenSP, page, size));
    }

    @GetMapping("/search/seller/{maGHDT}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> searchProductByNameAndMaGHDT(
            @PathVariable Long maGHDT,
            @RequestParam String tenSP,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(productService.searchProductByNameAndMaGHSH(maGHDT, tenSP, page, size));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("productDto") ProductRequestDto productRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ){
        return ResponseEntity.ok(productService.createProduct(productRequestDto, files));
    }


    @PatchMapping("/update/{maSP}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long maSP,
            @RequestPart ProductRequestDto productRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ){
        return ResponseEntity.ok(productService.updateProduct(maSP, productRequestDto, files));
    }

    @PostMapping("/disappear/{maSP}")
    public ResponseEntity<?> toggleProductVisibility(
            @PathVariable Long maSP
    ){
        return ResponseEntity.ok(productService.toggleProductVisibility(maSP));
    }

    @DeleteMapping("/delete/{maSP}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long maSP
    ){
        return ResponseEntity.ok(productService.deleteProduct(maSP));
    }

    @GetMapping("user/{maGHSH}")
    public ResponseEntity<?> getProductByStoreId(
            @PathVariable Long maGHSH,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(productService.getProductByStoreId(maGHSH, page, size));
    }

    @GetMapping("/user/notHidden/{maGHSH}")
    public ResponseEntity<?> getProductByStoreIdNotHidden(
            @PathVariable Long maGHSH,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(productService.getProductByStoreIdNotHidden(maGHSH, page, size));
    }
}
