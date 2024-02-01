package com.insight.controledejornada.model;

import com.insight.controledejornada.dto.WorkTimeDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

import static com.insight.controledejornada.utils.HourUtils.getLocalTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Interval {

    private final LocalTime input;

    private final LocalTime output;
}
