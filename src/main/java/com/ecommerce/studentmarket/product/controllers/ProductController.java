package com.ecommerce.studentmarket.product.controllers;


import com.ecommerce.studentmarket.product.dtos.ProductDto;
import com.ecommerce.studentmarket.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(productService.getAllProduct(page, size));
    }

    @GetMapping("/{maSP}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long maSP
    ){
        return ResponseEntity.ok(productService.getProductResponetById(maSP));

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductByName(
            @RequestParam String tenSP,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(productService.searchProductByName(tenSP, page, size));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("productDto") ProductDto productDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ){
        return ResponseEntity.ok(productService.createProduct(productDto, files));
    }


    @PatchMapping("/update/{maSP}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long maSP,
            @RequestPart ProductDto productDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ){
        return ResponseEntity.ok(productService.updateProduct(maSP, productDto, files));
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
