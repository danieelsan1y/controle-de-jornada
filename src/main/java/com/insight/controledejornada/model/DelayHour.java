package com.insight.controledejornada.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class DelayHour implements Comparable<DelayHour> {

    private final LocalTime start;

    private final LocalTime end;

    @Override
    public int compareTo(DelayHour o) {
        return o.getStart().compareTo(this.start);
    }
}
