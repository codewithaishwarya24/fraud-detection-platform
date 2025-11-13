package com.fraud.merchant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.merchant.model.MerchantDTO;
import com.fraud.merchant.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MerchantController.class)
public class MerchantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Autowired
    private ObjectMapper objectMapper;

    // -----------------------------
    // Test: GET /api/merchants/health
    // -----------------------------
    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/merchants/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Merchant service running OK"));
    }

    // -----------------------------
    // Test: POST /api/merchants/create
    // -----------------------------
    @Test
    void testCreateMerchant() throws Exception {

        MerchantDTO dto = new MerchantDTO();
        dto.setMerchantId("M100");
        dto.setName("Test Merchant");

        Mockito.when(merchantService.createMerchant(Mockito.any()))
                .thenReturn(ResponseEntity.ok("Merchant created Successfully"));

        mockMvc.perform(post("/api/merchants/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Merchant created Successfully"));
    }

    // -----------------------------
    // Test: GET /api/merchants/{merchantId}
    // -----------------------------
    @Test
    void testGetMerchantById() throws Exception {

        MerchantDTO dto = new MerchantDTO();
        dto.setMerchantId("M100");
        dto.setName("Test Merchant");

        Mockito.when(merchantService.getMerchantById("M100"))
                .thenReturn(ResponseEntity.ok(dto));

        mockMvc.perform(get("/api/merchants/M100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantId").value("M100"))
                .andExpect(jsonPath("$.name").value("Test Merchant"));
    }
}
