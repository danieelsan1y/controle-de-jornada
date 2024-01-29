package com.insight.controledejornada.controledejornada.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MarkedTime extends Time {


    public MarkedTime(LocalTime input, LocalTime output) {
        super(input, output);
    }
}
