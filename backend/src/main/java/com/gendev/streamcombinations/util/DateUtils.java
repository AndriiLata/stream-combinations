package com.gendev.streamcombinations.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static LocalDateTime parse(String dateTimeString) {
        if(dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        LocalDateTime localDateTime = null;
        try {
            if (dateTimeString.length() == 10) { // yyyy-MM-dd

                localDateTime = LocalDateTime.parse(dateTimeString + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (dateTimeString.length() == 19) { // yyyy-MM-dd HH:mm:ss
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                localDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
            } else {
                throw new IllegalArgumentException("Invalid date format.");
            }
        } catch (DateTimeParseException | IllegalArgumentException e) {
            System.out.println("Invalid date format or value: " + dateTimeString);
        }
        return localDateTime;
    }

}
