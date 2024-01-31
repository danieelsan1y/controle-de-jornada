package com.insight.controledejornada.model;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.utils.HourUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

import static com.insight.controledejornada.utils.HourUtils.getLocalTime;

@Getter
@Setter
@ToString
public class WorkTime extends Time {

    public WorkTime(LocalTime input, LocalTime output) {
        super(null, input, output);
    }

    public WorkTime(WorkTimeDTO dto) {
        super(dto.getId(), getLocalTime(dto.getInput()), getLocalTime(dto.getOutput()));
    }
}
