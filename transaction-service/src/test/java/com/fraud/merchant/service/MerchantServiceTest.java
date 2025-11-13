package com.fraud.merchant.service;

import com.fraud.merchant.entity.Merchant;
import com.fraud.merchant.model.MerchantDTO;
import com.fraud.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    private MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

    private MerchantDTO merchantDTO;
    private Merchant merchant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        merchantDTO = new MerchantDTO();
        merchantDTO.setMerchantId("M001");
        merchantDTO.setName("Test Merchant");

        merchant = new Merchant();
        merchant.setMerchantId("M001");
        merchant.setName("Test Merchant");
    }

    @Test
    void testCreateMerchant_Success() {
        // Arrange
        when(merchantRepository.existsByMerchantId("M001")).thenReturn(false);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        // Act
        ResponseEntity<String> response = merchantService.createMerchant(merchantDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Merchant created Successfully", response.getBody());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void testCreateMerchant_WhenMerchantIdAlreadyExists() {
        // Arrange
        when(merchantRepository.existsByMerchantId("M001")).thenReturn(true);

        // Act
        ResponseEntity<String> response = merchantService.createMerchant(merchantDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Merchant ID already exists", response.getBody());
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    @Test
    void testGetMerchantById_Success() {
        // Arrange
        when(merchantRepository.findMerchantsByMerchantId(eq("M001"))).thenReturn(merchant);

        // Act
        ResponseEntity<MerchantDTO> response = merchantService.getMerchantById("M001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("M001", response.getBody().getMerchantId());
        assertEquals("Test Merchant", response.getBody().getName());
    }

    @Test
    void testCreateMerchant_SetsTimestamps() {
        // Arrange
        when(merchantRepository.existsByMerchantId("M001")).thenReturn(false);
        when(merchantRepository.save(any(Merchant.class))).thenAnswer(invocation -> {
            Merchant saved = invocation.getArgument(0);
            assertNotNull(saved.getCreatedAt(), "createdAt should be set");
            assertNotNull(saved.getUpdatedAt(), "updatedAt should be set");
            return saved;
        });

        // Act
        ResponseEntity<String> response = merchantService.createMerchant(merchantDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }
}
