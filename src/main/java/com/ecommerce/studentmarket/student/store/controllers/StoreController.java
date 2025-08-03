package com.ecommerce.studentmarket.student.store.controllers;


import com.ecommerce.studentmarket.student.store.dtos.StoreDtoRequest;
import com.ecommerce.studentmarket.student.store.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/customer/{msh}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getStoreByMsh(
            @PathVariable String msh,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(storeService.getStoreByMsh(msh, page, size));
    }

    @GetMapping("/seller/{msh}")
    @PreAuthorize("hasRole('admin') or #msh == authentication.name")
    public ResponseEntity<?> getStoreByMshAndProductNotHidden(
            @PathVariable String msh,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(storeService.getStoreByMshAndProductNotHidden(msh, page, size));
    }

    @PatchMapping("/seller/{msh}")
    @PreAuthorize("hasRole('admin') or #msh == authentication.name")
    public ResponseEntity<?> updateStore(
            @PathVariable String msh,
            @RequestBody StoreDtoRequest storeDtoRequest
            ){
        return ResponseEntity.ok(storeService.updateStore(msh, storeDtoRequest));
    }


}
