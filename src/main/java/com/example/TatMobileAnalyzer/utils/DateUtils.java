package com.example.TatMobileAnalyzer.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Getter
public class DateUtils {

    @Getter
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date parseDate(String dateString, String pattern) {
        if (dateString == null || pattern == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            formatter.setLenient(false);
            return formatter.parse(dateString);
        } catch (Exception e) {
            log.error("Error parsing date: {}", e.getMessage());
            return null;
        }
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

}

