package com.insight.controledejornada.model;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.utils.HourUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import static com.insight.controledejornada.utils.HourUtils.getLocalTime;

@Getter
@Setter
public class MarkedTime extends Time {

    public MarkedTime(LocalTime input, LocalTime output) {
        super(null, input, output);
    }


    public MarkedTime(MarkedTimeDTO dto) {
        super(dto.getId(), getLocalTime(dto.getInput()), getLocalTime(dto.getOutput()));
    }
}