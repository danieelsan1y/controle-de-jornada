package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.model.DelayHour;
import com.insight.controledejornada.model.ExtraHour;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.DelayHourService;
import lombok.RequiredArgsConstructor;

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

        for (WorkTime workTime : workTimes) {
            for (MarkedTime markedTime : markedTimes) {
                if ((workTime.getInput().isBefore(markedTime.getOutput()) && workTime.getOutput().isAfter(markedTime.getInput()))) {
                    if (markedTime.getInput().isAfter(workTime.getInput())) {
                        delayHours.add(new DelayHour(workTime.getInput(), markedTime.getInput()));
                    }

                    if (markedTime.getOutput().isBefore(workTime.getOutput())) {
                        delayHours.add(new DelayHour(markedTime.getOutput(), workTime.getOutput()));
                    }
                }
            }
        }

        return delayHours;
    }
}
