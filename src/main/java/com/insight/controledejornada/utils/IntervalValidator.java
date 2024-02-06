package com.insight.controledejornada.utils;

import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.Time;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.insight.controledejornada.exception.Message.*;

public class IntervalValidator {

    public static void validateInterval(
            Time time,
            List<Time> times,
            Optional<Time> removed
    ) {
        if (time.getInput().equals(time.getOutput())) {
            throw new SystemException(INPUT_AND_OUTPUT_EQUALS);
        }

        removed.flatMap(it -> times.stream()
                        .filter(listTime -> listTime.getId().equals(it.getId()))
                        .findFirst())
                .ifPresent(times::remove);

        if (!time.spansToNextDay()) {
            times.stream()
                    .filter(Objects::nonNull)
                    .forEach(it -> {
                        if (time.getInput().compareTo(it.getInput()) > -1
                                && time.getInput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                        if (time.getOutput().compareTo(it.getInput()) > -1
                                && time.getOutput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                        if (!it.spansToNextDay() && time.getInput().isBefore(it.getInput())
                                && time.getOutput().isAfter(it.getOutput())) {
                            throw new SystemException(TIME_OVERLAY);
                        }
                        if (it.spansToNextDay() && time.getInput().isBefore(it.getInput())
                                && time.getOutput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                        if (it.spansToNextDay() && time.getInput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                    });
        } else {
            times.stream()
                    .filter(Objects::nonNull)
                    .filter(Time::spansToNextDay)
                    .forEach(it -> {
                        if (time.getInput().compareTo(it.getInput()) > -1
                                && time.getInput().compareTo(it.getOutput()) > -1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                        if (time.getOutput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                        if (time.getInput().isBefore(it.getInput()) && time.getOutput().isAfter(it.getOutput())) {
                            throw new SystemException(TIME_OVERLAY);
                        }
                        if (time.getInput().compareTo(it.getInput()) > -1 && time.getOutput().compareTo(it.getOutput()) < 1) {
                            throw new SystemException(TIME_CONFLICT);
                        }
                    });
        }
    }
}
