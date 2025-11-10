package com.fraud.merchant.controller;

import com.fraud.merchant.entity.Merchant;
import com.fraud.merchant.model.MerchantDTO;
import com.fraud.merchant.service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/health")
    public String health() {
        return "Merchant service running OK";
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMerchants(@RequestBody MerchantDTO merchantDTO) {
        return merchantService.createMerchant(merchantDTO);
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<MerchantDTO> getMerchantById(@PathVariable("merchantId") String merchantId) {
        return merchantService.getMerchantById(merchantId);
    }
   }
