package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.DelayHourDTO;
import com.insight.controledejornada.model.Interval;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.DelayHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DelayHourServiceImpl implements DelayHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    @Override
    public List<DelayHourDTO> getDelayHours() {
        final List<WorkTime> workTimes = workTimeRepository.listAll()
                .stream()
                .sorted(Comparator.comparing(WorkTime::getInput))
                .collect(Collectors.toList());

        final List<MarkedTime> markedTimes = markedTimeRepository.listAll()
                .stream()
                .sorted(Comparator.comparing(MarkedTime::getInput))
                .collect(Collectors.toList());

        final List<DelayHourDTO> delayHourDTOS = new ArrayList<>(0);

        for (WorkTime workTime : workTimes) {
            if (workTime.spansToNextDay()) {
                for (MarkedTime markedTime : markedTimes) {
                    this.processSpansToNextDay(workTime, markedTime, delayHourDTOS);
                }
            } else {
                this.processNotSpansToNextDay(workTime, markedTimes, delayHourDTOS);
            }
        }

        return delayHourDTOS;
    }

    private void processNotSpansToNextDay(
            WorkTime workTime,
            List<MarkedTime> markedTimes,
            List<DelayHourDTO> delayHourDTOS
    ) {
        final Optional<MarkedTime> markedTime = markedTimes.stream()
                .filter(it -> !it.spansToNextDay())
                .filter(it -> (workTime.getOutput().isAfter(it.getOutput()) || workTime.getOutput().equals(it.getOutput()))
                        && workTime.getInput().isBefore(it.getOutput())
                )
                .findFirst();

        if (markedTime.isPresent()) {
            if ((markedTime.get().getInput().isAfter(workTime.getInput()))) {
                delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), markedTime.get().getInput()));
            }
            if (markedTime.get().getOutput().isBefore(workTime.getOutput())) {
                delayHourDTOS.add(new DelayHourDTO(markedTime.get().getOutput(), workTime.getOutput()));
            }
        } else {
            final Optional<MarkedTime> equals = markedTimes.stream()
                    .filter(it -> !it.spansToNextDay())
                    .filter(it -> it.getInput().equals(workTime.getInput())
                            && it.getOutput().equals(workTime.getOutput())
                    )
                    .findFirst();

            final Optional<MarkedTime> greater = markedTimes.stream()
                    .filter(it -> it.getInput().compareTo(workTime.getInput()) < 1
                            && it.getOutput().compareTo(workTime.getOutput()) > -1
                    )
                    .filter(it -> !it.spansToNextDay())
                    .findFirst();

            if (equals.isEmpty() && greater.isEmpty()) {
                delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
            }
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
