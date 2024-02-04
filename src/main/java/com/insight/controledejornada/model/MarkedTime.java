package com.insight.controledejornada.model;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import static com.insight.controledejornada.utils.HourUtils.getLocalTime;

@Getter
@Setter
public class MarkedTime extends Time implements Comparable<MarkedTime> {

    public final static String pageName = "markedTime.jsp";

    public MarkedTime(Long id, LocalTime input, LocalTime output) {
        super(id, input, output);
    }

    public MarkedTime(LocalTime input, LocalTime output) {
        super(null, input, output);
    }

    public MarkedTime(MarkedTimeDTO dto) {
        super(dto.getId(), getLocalTime(dto.getInput()), getLocalTime(dto.getOutput()));
    }

    @Override
    public int compareTo(MarkedTime o) {
        return o.getInput().compareTo(this.getInput());
    }
}
