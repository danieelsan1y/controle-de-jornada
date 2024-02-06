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
        final List<Interval> intervals = this.getIntervals(workTimes);

        if (workTimes.isEmpty() || markedTimes.isEmpty()) {
            return new ArrayList<>(0);
        }

        for (int indexWorkTime = 0; indexWorkTime < workTimes.size(); indexWorkTime++) {
            for (int indexMarkedTime = 0; indexMarkedTime < markedTimes.size(); indexMarkedTime++) {
                this.process(
                        workTimes.get(indexWorkTime),
                        markedTimes.get(indexMarkedTime),
                        delayHourDTOS,
                        indexMarkedTime,
                        indexWorkTime,
                        intervals
                );
            }
        }

        return delayHourDTOS;
    }

    private List<Interval> getIntervals(List<WorkTime> workTimes) {
        int i = 0;
        final List<Interval> intervals = new ArrayList<>(0);
        while (i < workTimes.size() - 1) {
            intervals.add(new Interval(workTimes.get(i).getOutput(), workTimes.get(i + 1).getInput()));
            i++;
        }
        return intervals;
    }

    private void process(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS,
            int indexMarkedTime,
            int indexWorkTime,
            List<Interval> intervals
    ) {
        if (workTime.spansToNextDay()) {
            this.processSpansToNextDay(workTime, markedTime, delayHourDTOS);
        } else {
            this.processNotSpansToNextDay(workTime, markedTime, delayHourDTOS, indexMarkedTime, indexWorkTime, intervals);
        }
    }

    private void processNotSpansToNextDay(
            WorkTime workTime,
            MarkedTime markedTime,
            List<DelayHourDTO> delayHourDTOS,
            int indexMarkedTime,
            int indexWorkTime,
            List<Interval> intervals
    ) {
        final List<LocalTime> localTimes = new ArrayList<>(0);
        this.setLocalTimes(markedTime, localTimes);

        final Optional<LocalTime> first = localTimes.stream()
                .filter(it -> it.compareTo(workTime.getInput()) > -1)
                .findFirst();

        final List<LocalTime> localTimesFiltered = localTimes.stream()
                .filter(it -> it.compareTo(workTime.getOutput()) < 1)
                .collect(Collectors.toList());

        final Function<LocalTime, Boolean> isOnRange = it -> intervals.stream()
                .anyMatch(interval -> interval.getInput().compareTo(it) > -1 && interval.getOutput().compareTo(it) < 1);

        if (first.isPresent() && first.get().isAfter(workTime.getInput())
                && (indexMarkedTime == indexWorkTime || delayHourDTOS.isEmpty())) {
            if (isOnRange.apply(first.get()) && workTime.getOutput().isBefore(first.get())) {
                delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
            } else {
                if (markedTime.getInput().isAfter(workTime.getOutput())) {
                    delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
                } else {
                    delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), first.get()));
                }
            }
        }

        if (!localTimesFiltered.isEmpty() &&
                localTimesFiltered.get(localTimesFiltered.size() - 1).isBefore(workTime.getOutput())
                && (indexMarkedTime == indexWorkTime)) {
            if (isOnRange.apply(localTimesFiltered.get(localTimesFiltered.size() - 1))) {
                delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
            } else {
                delayHourDTOS.add(new DelayHourDTO(localTimesFiltered.get(localTimesFiltered.size() - 1), workTime.getOutput()));
            }
        }
    }

    private void setLocalTimes(MarkedTime markedTime, List<LocalTime> localTimes) {
        localTimes.add(markedTime.getInput());

        while (localTimes.get(localTimes.size() - 1).plusHours(1).isBefore(markedTime.getOutput())) {
            localTimes.add(localTimes.get(localTimes.size() - 1).plusHours(1));
        }

        localTimes.add(markedTime.getOutput());
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
