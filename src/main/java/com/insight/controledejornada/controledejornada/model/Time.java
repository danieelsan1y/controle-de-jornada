package com.insight.controledejornada.controledejornada.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class Time {

    private LocalTime input;

    private LocalTime output;
}
