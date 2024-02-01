package com.insight.controledejornada.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExtraHour implements Comparable<ExtraHour> {


    private final LocalTime start;

    private final LocalTime end;

    @Override
    public int compareTo(ExtraHour o) {
        return o.getStart().compareTo(this.start);
    }
}
