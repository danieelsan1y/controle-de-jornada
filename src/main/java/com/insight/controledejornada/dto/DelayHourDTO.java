package com.insight.controledejornada.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class DelayHourDTO implements Comparable<DelayHourDTO> {

    private final LocalTime start;

    private final LocalTime end;

    @Override
    public int compareTo(DelayHourDTO o) {
        return o.getStart().compareTo(this.start);
    }
}
