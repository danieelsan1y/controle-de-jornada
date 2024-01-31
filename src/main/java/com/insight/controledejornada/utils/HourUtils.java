package com.insight.controledejornada.utils;

import com.insight.controledejornada.exception.SystemException;

import java.time.LocalTime;

import static java.lang.Integer.parseInt;

public class HourUtils {

    private final static String message = "Formato de hora e minuto incorreto";

    public static LocalTime getLocalTime(String content) {
        final String message = "Formato de hora e minuto incorreto";

        if (!content.contains(":")) {
            throw new SystemException(message);
        }

        final String[] hourAndMinute = content.split(":");


        if (hourAndMinute.length != 2) {
            throw new SystemException(message);
        }


        final String hour = hourAndMinute[0];
        final String minute = hourAndMinute[1];

        validate(hour);
        validate(minute);


        return LocalTime.of(parseInt(hour), parseInt(minute));
    }

    private static void validate(String content) {
        if(!content.matches("[0-9]+") || content.length() != 2){
            throw new SystemException(message);
        }
        final int number = Integer.parseInt(content);

        if (number < 0 || number > 24) {
            throw new SystemException(message);
        }
    }
}
