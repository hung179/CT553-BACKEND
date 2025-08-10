package com.ecommerce.studentmarket.student.ewallet.controllers;


import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionRequestDto;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionResponseDto;
import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("wallet")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{mssv}")
    public ResponseEntity<?> payTheOrder(
            @PathVariable String mssv,
            @RequestBody TransactionRequestDto transactionRequestDto
            ){
        return ResponseEntity.ok(paymentService.handlePayTheOrder(mssv, transactionRequestDto));
    }

    @GetMapping("/{mssv}")
    public ResponseEntity<?> getEwalletInfo(
            @PathVariable String mssv
    ){
        return ResponseEntity.ok(paymentService.getEwalletInfo(mssv));
    }
    @GetMapping("/transactions/{maVDT}")
    public ResponseEntity<?> getTransactions(
            @PathVariable Long maVDT,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("thoiGianGD").descending());
        Page<TransactionResponseDto> result = paymentService.getPagedTransactionsByEwallet(maVDT, pageable);
        return ResponseEntity.ok(result);
    }
}
