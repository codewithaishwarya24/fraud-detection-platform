package com.fraud.merchant.service;

import com.fraud.merchant.entity.Merchant;
import com.fraud.merchant.model.MerchantDTO;
import com.fraud.merchant.repository.MerchantRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerchantService {
    MerchantRepository merchantRepository;
    MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

    public ResponseEntity<String> createMerchant(MerchantDTO merchantDTO) {
        Merchant merchant = merchantMapper.mapMerchantDTOtoMerchant(merchantDTO);

        if (merchantRepository.existsByMerchantId(merchantDTO.getMerchantId())) {
            throw new IllegalArgumentException("Merchant ID already exists");
        }
        merchant.setCreatedAt(LocalDateTime.now());
        merchant.setUpdatedAt(LocalDateTime.now());
        merchantRepository.save(merchant);

        return new ResponseEntity<>("Merchant created Successfully",HttpStatus.CREATED);
    }

    public List<Merchant> getMerchantById(Long merchantId) {
        return merchantRepository.findMerchantsById(merchantId);
    }
}
