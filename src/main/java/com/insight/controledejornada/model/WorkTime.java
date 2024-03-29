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
public class WorkTime extends Time implements Comparable<WorkTime> {

    public final static String pageName = "workTime.jsp";

    public WorkTime(Long id, LocalTime input, LocalTime output) {
        super(id, input, output);
    }

    public WorkTime(LocalTime input, LocalTime output) {
        super(null, input, output);
    }

    public WorkTime(WorkTimeDTO dto) {
        super(dto.getId(), getLocalTime(dto.getInput()), getLocalTime(dto.getOutput()));
    }

    @Override
    public int compareTo(WorkTime o) {
        return o.getInput().compareTo(this.getInput());
    }
}
