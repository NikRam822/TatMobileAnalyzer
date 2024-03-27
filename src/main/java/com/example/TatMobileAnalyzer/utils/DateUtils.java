package com.example.TatMobileAnalyzer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


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

