package com.example.TatMobileAnalyzer.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateUtilsTest {

    private static final String TEST_DATE_STRING = "2023-07-17 10:30:00";
    private static final String TEST_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Test
    void testParseDate() {
        Date date = DateUtils.parseDate(TEST_DATE_STRING, TEST_DATE_FORMAT);
        assertNotNull(date);
        assertEquals(TEST_DATE_STRING, DateUtils.formatDate(date, TEST_DATE_FORMAT));
    }

    @Test
    void testParseDateWithNullInput() {
        assertNull(DateUtils.parseDate(null, TEST_DATE_FORMAT));
    }

    @Test
    void testParseDateWithNullPattern() {
        assertNull(DateUtils.parseDate(TEST_DATE_STRING, null));
    }

    @Test
    void testParseDateWithInvalidFormat() {
        assertNull(DateUtils.parseDate(TEST_DATE_STRING, "invalid-format"));
    }

    @Test
    void testParseDateWithInvalidDate() {
        assertNull(DateUtils.parseDate("invalid-date", TEST_DATE_FORMAT));
    }

    @Test
    void testFormatDate() {
        Date now = new Date();
        String formattedDate = DateUtils.formatDate(now, TEST_DATE_FORMAT);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void testFormatDateWithNullInput() {
        assertNull(DateUtils.formatDate(null, TEST_DATE_FORMAT));
    }

    @Test
    void testGetDateFormat() {
        assertEquals("yyyy-MM-dd HH:mm:ss", DateUtils.getDATE_FORMAT());
    }
}