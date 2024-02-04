package com.insight.controledejornada.utils;

import com.insight.controledejornada.exception.SystemException;

import java.time.LocalTime;

import static com.insight.controledejornada.exception.Message.WRONG_HOUR_AND_MINUTE;
import static java.lang.Integer.parseInt;

public class HourUtils {


    public static LocalTime getLocalTime(String content) {
        if (!content.contains(":")) {
            throw new SystemException(WRONG_HOUR_AND_MINUTE);
        }

        final String[] hourAndMinute = content.split(":");

        if (hourAndMinute.length != 2) {
            throw new SystemException(WRONG_HOUR_AND_MINUTE);
        }

        final String hour = hourAndMinute[0];
        final String minute = hourAndMinute[1];

        validate(hour, "hour");
        validate(minute, "minute");

        return LocalTime.of(parseInt(hour), parseInt(minute));
    }

    private static void validate(String content, String type) {
        if (!content.matches("[0-9]+") || content.length() != 2) {
            throw new SystemException(WRONG_HOUR_AND_MINUTE);
        }
        final int number = Integer.parseInt(content);

        if (type.equals("hour") && (number < 0 || number > 24)) {
            throw new SystemException(WRONG_HOUR_AND_MINUTE);
        }

        if (type.equals("minute") && (number < 0 || number > 59)) {
            throw new SystemException(WRONG_HOUR_AND_MINUTE);
        }
    }
}
