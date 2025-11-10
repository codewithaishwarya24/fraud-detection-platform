package com.fraud.merchant.service;

import com.fraud.merchant.entity.Merchant;
import com.fraud.merchant.model.MerchantDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantMapper {
    MerchantDTO mapMerchantToMerchantDTO(Merchant merchant);

    Merchant mapMerchantDTOtoMerchant(MerchantDTO merchantDTO);
}

