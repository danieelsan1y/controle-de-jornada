package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.DelayHourDTO;
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
    public List<DelayHourDTO> getDelayHours() {
        final List<WorkTime> workTimes = workTimeRepository.listAll();
        final List<MarkedTime> markedTimes = markedTimeRepository.listAll();
        final List<DelayHourDTO> delayHourDTOS = new ArrayList<>(0);

        if(workTimes.isEmpty() || markedTimes.isEmpty()) {
            return new ArrayList<>(0);
        }

        for (WorkTime workTime : workTimes) {
            for (MarkedTime markedTime : markedTimes) {
                this.process(workTime, markedTime, delayHourDTOS);
            }
        }

        return delayHourDTOS;
    }

    private void process(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (workTime.spansToNextDay()) {
            this.processSpansToNextDay(workTime, markedTime, delayHourDTOS);
        } else {
            this.processNotSpansToNextDay(workTime, markedTime, delayHourDTOS);
        }
    }

    private void processNotSpansToNextDay(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if ((workTime.getInput().isBefore(markedTime.getOutput())
                && workTime.getOutput().isAfter(markedTime.getInput()))) {
            this.setMarkedTimeAsEntryAndExitDelays(workTime, markedTime, delayHourDTOS);
        }
    }

    private void processSpansToNextDay(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (workTime.getInput().isAfter(markedTime.getOutput())
                && workTime.getOutput().isBefore(markedTime.getInput())) {
            this.setMarkedTimeAsEntryAndExitDelays(workTime, markedTime, delayHourDTOS);
        }
        if (workTime.getInput().isAfter(markedTime.getOutput())
                && workTime.getOutput().isAfter(markedTime.getInput())) {
            this.processWorkTimeAsEntryAndExitDelays(workTime, markedTime, delayHourDTOS);
        }
    }

    private void setMarkedTimeAsEntryAndExitDelays(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (markedTime.getInput().isAfter(workTime.getInput())) {
            delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), markedTime.getInput()));
        }

        if (markedTime.getOutput().isBefore(workTime.getOutput())) {
            delayHourDTOS.add(new DelayHourDTO(markedTime.getOutput(), workTime.getOutput()));
        }
    }

    private void processWorkTimeAsEntryAndExitDelays(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (workTime.getInput().isAfter(markedTime.getInput())) {
            delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), markedTime.getInput()));
        }

        if (workTime.getOutput().isBefore(workTime.getOutput())) {
            delayHourDTOS.add(new DelayHourDTO(markedTime.getOutput(), workTime.getOutput()));
        }
    }
}
