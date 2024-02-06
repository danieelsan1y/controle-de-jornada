package com.insight.controledejornada.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public abstract class Time {

    private Long id;

    private LocalTime input;

    private LocalTime output;

    public boolean spansToNextDay() {
        return this.getInput().isAfter(this.getOutput());
    }

}
