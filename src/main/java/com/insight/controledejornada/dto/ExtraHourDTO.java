package com.insight.controledejornada.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExtraHourDTO implements Comparable<ExtraHourDTO> {

    private final LocalTime start;

    private final LocalTime end;

    @Override
    public int compareTo(ExtraHourDTO o) {
        return o.getStart().compareTo(this.start);
    }
}
