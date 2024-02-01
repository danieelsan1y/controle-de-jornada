package com.insight.controledejornada.model;

import com.insight.controledejornada.dto.WorkTimeDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

import static com.insight.controledejornada.utils.HourUtils.getLocalTime;

@Getter
@Setter
@ToString
public class WorkTime extends Time {

    public WorkTime(Long id, LocalTime input, LocalTime output) {
        super(id, input, output);
    }

    public WorkTime(LocalTime input, LocalTime output) {
        super(null, input, output);
    }

    public WorkTime(WorkTimeDTO dto) {
        super(dto.getId(), getLocalTime(dto.getInput()), getLocalTime(dto.getOutput()));
    }

    public boolean spansToNextDay() {
        return this.getInput().isAfter(this.getOutput());
    }

}
