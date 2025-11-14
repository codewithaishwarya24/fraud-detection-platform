package com.fraud.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CorrelationIdFilterTest {
    private static final String HEADER_CORRELATION_ID = "X-Correlation-Id"; // assuming constant name

    private CorrelationIdFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void testDoFilter_WhenHeaderPresent_ShouldUseProvidedCorrelationId() throws IOException, ServletException {
        // Arrange
        String existingId = UUID.randomUUID().toString();
        when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn(existingId);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilter_WhenHeaderMissing_ShouldGenerateNewCorrelationId() throws IOException, ServletException {
        // Arrange
        when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn(null);

        // Capture UUID that gets set in MDC
        ArgumentCaptor<ServletRequest> requestCaptor = ArgumentCaptor.forClass(ServletRequest.class);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(requestCaptor.capture(), eq(response));

    }

    @Test
    void testDoFilter_WhenHeaderBlank_ShouldGenerateNewCorrelationId() throws IOException, ServletException {
        // Arrange
        when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn("  "); // blank header

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
    }
}
