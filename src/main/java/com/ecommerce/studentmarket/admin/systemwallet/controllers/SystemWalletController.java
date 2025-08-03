package com.ecommerce.studentmarket.admin.systemwallet.controllers;

import com.ecommerce.studentmarket.admin.systemwallet.services.SystemWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("systemwallet")
public class SystemWalletController {

    @Autowired
    private SystemWalletService systemWalletService;


    @GetMapping()
    public ResponseEntity<?> getSystemWallet(){
        return ResponseEntity.ok(systemWalletService.getSystemWallet());
    }

    @GetMapping("/{mVHT}")
    public ResponseEntity<?> getPagedSystemTransactions(
            @PathVariable Long mVHT,
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("thoiGianGDHT").descending());
        return ResponseEntity.ok(systemWalletService.getPagedSystemTransactionsBySystemWallet(mVHT, pageable));
    }
}
