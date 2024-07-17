package com.example.TatMobileAnalyzer.utils;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Getter
public class DateUtils {

    @Getter
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date parseDate(String dateString, String pattern) throws ParseException {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(dateString);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

}

