package br.com.alura.techcase.core.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SiteUtil {

    public static String formatDate (LocalDate date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatter);
        }
        return "";
    }
}
