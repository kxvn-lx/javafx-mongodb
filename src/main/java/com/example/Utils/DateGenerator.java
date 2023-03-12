package com.example.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateGenerator {
    private static final LocalDate currentDate = LocalDate.now();

    private DateGenerator() { }

    /**
     * Returns the current date as a formatted String.
     *
     * @return a String representing the current date in the format "dd-MM-yyyy"
     */
    public static String getCurrentDate() {
        // Create a DateTimeFormatter to format the date as "dd-MM-yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Format the date as a String using the formatter
        return currentDate.format(formatter);
    }
}
