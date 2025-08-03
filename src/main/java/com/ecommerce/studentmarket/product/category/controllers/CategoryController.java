package com.ecommerce.studentmarket.product.category.controllers;

import com.ecommerce.studentmarket.product.category.dtos.CategoryRequestDto;
import com.ecommerce.studentmarket.product.category.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getAllCategory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(categoryService.getAllCategory(page, size));
    }

    @GetMapping("byId/{maDM}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getCategoryById(
            @PathVariable Long maDM
    ){
        return ResponseEntity.ok(categoryService.getCategoryById(maDM));

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createCategory(
            @RequestBody CategoryRequestDto categoryRequestDto
    ){
        return ResponseEntity.ok(categoryService.createCategory(categoryRequestDto));
    }

    @PatchMapping("/update/{maDM}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long maDM,
            @RequestBody CategoryRequestDto categoryRequestDto
    ){
        return ResponseEntity.ok(categoryService.updateCategory(maDM, categoryRequestDto));
    }

    @DeleteMapping("/delete/{maDM}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long maDM
    ){
        return ResponseEntity.ok(categoryService.deleteCategory(maDM));
    }

}
