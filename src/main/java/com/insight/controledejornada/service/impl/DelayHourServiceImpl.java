package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.model.DelayHour;
import com.insight.controledejornada.model.ExtraHour;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.DelayHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DelayHourServiceImpl implements DelayHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    @Override
    public List<DelayHour> getDelayHours() {
        final List<WorkTime> workTimes = workTimeRepository.listAll();
        final List<MarkedTime> markedTimes = markedTimeRepository.listAll();
        final List<DelayHour> delayHours = new ArrayList<>(0);

        this.calculateDelayHours(workTimes, markedTimes, delayHours);

        return delayHours;
    }

    private void calculateDelayHours(List<WorkTime> workTimes, List<MarkedTime> markedTimes, List<DelayHour> delayHours) {
        for (WorkTime workTime : workTimes) {
            for (MarkedTime markedTime : markedTimes) {
                if (hasOverlap(workTime.getInput(), workTime.getOutput(), markedTime.getInput(), markedTime.getOutput())) {
                    processDelayHours(delayHours, workTime, markedTime);
                }
            }
        }
    }

    private void processDelayHours(List<DelayHour> delayHours, WorkTime workTime, MarkedTime markedTime) {
        if (markedTime.getInput().isAfter(workTime.getInput())) {
            delayHours.add(new DelayHour(workTime.getInput(), markedTime.getInput()));
        }

        if (markedTime.getOutput().isBefore(workTime.getOutput())) {
            delayHours.add(new DelayHour(markedTime.getOutput(), workTime.getOutput()));
        }
    }

    private boolean hasOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) || start1.equals(end2)) && (end1.isAfter(start2) || end1.equals(start2));
    }


    private boolean isOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) || start1.equals(end2)) && (end1.isAfter(start2) || end1.equals(start2));
    }
}
