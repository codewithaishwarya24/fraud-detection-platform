package com.fraud.common.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MaskingUtilTest {

    @Test
    void testMaskCard_WhenInputIsNull_ShouldReturnNull() {
        assertThat(MaskingUtil.maskCard(null)).isNull();
    }

    @Test
    void testMaskCard_WhenLengthIsLessThanOrEqualTo4_ShouldReturnSameValue() {
        assertThat(MaskingUtil.maskCard("1234")).isEqualTo("1234");
        assertThat(MaskingUtil.maskCard("12")).isEqualTo("12");
    }

    @Test
    void testMaskCard_WhenLengthGreaterThan4_ShouldMaskProperly() {
        String masked = MaskingUtil.maskCard("1234567812345678");
        assertThat(masked).isEqualTo("**** **** **** 5678");
    }
}
