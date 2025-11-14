package com.fraud.common.util;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilTest {

    @Test
    void testIsoFormatter_ShouldBeSameAsJavaIsoFormatter() {
        // Verify constant is not null and matches the standard formatter
        assertThat(DateUtil.ISO).isNotNull();
        assertThat(DateUtil.ISO).isEqualTo(DateTimeFormatter.ISO_DATE_TIME);
    }
}
