package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.ExtraHourDTO;
import com.insight.controledejornada.model.*;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.ExtraHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExtraHourServiceImpl implements ExtraHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    public List<ExtraHourDTO> getExtraHours() {
        final List<WorkTime> workTimes = this.workTimeRepository.listAll();
        final List<MarkedTime> markedTimes = this.markedTimeRepository.listAll();

        if(workTimes.isEmpty() || markedTimes.isEmpty()) {
            return new ArrayList<>(0);
        }

        final List<ExtraHourDTO> extraHourDTOS = new ArrayList<>(0);
        final List<Interval> intervals = getIntervals(workTimes);

        final WorkTime first = workTimes.stream().findFirst().orElseThrow(RuntimeException::new);
        final WorkTime last = workTimes.get(workTimes.size() - 1);

        this.setExtraHours(markedTimes, first, extraHourDTOS, intervals, last);

        return extraHourDTOS.stream()
                .sorted(Comparator.comparing(ExtraHourDTO::getStart))
                .collect(Collectors.toList());
    }

    private void setExtraHours(
            List<MarkedTime> markedTimes,
            WorkTime first,
            List<ExtraHourDTO> extraHourDTOS,
            List<Interval> intervals,
            WorkTime last
    ) {
        markedTimes.stream()
                .filter(Objects::nonNull)
                .forEach(it -> {
                    this.setOvertimeBeforeWork(first, extraHourDTOS, it);

                    this.setOvertimeDuringTheBreak(it, intervals, extraHourDTOS);

                    this.setOvertimeAfterWork(extraHourDTOS, last, it);
                });
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

    private void setOvertimeBeforeWork(
            WorkTime first,
            List<ExtraHourDTO> extraHourDTOS,
            MarkedTime markedTime
    ) {
        if (markedTime.getInput().isBefore(first.getInput())) {
            if (first.spansToNextDay() && markedTime.getInput().isAfter(first.getOutput())) {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), first.getInput()));
            } else if (!first.spansToNextDay()) {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), first.getInput()));
            }
        }
    }

    private void setOvertimeAfterWork(
            List<ExtraHourDTO> extraHourDTOS,
            WorkTime last,
            MarkedTime markedTime
    ) {
        if (last != null && markedTime.getOutput().isAfter(last.getOutput())) {
            extraHourDTOS.add(new ExtraHourDTO(last.getOutput(), markedTime.getOutput()));
        }
    }

    private void setOvertimeDuringTheBreak(
            MarkedTime markedTime,
            List<Interval> intervals,
            List<ExtraHourDTO> extraHourDTOS
    ) {
        final LocalTime markedTimeInput = markedTime.getInput();
        final LocalTime markedTimeOutput = markedTime.getOutput();
        intervals.stream()
                .filter(Objects::nonNull)
                .forEach(interval ->
                        this.processInterval(
                                markedTime,
                                extraHourDTOS,
                                interval,
                                markedTimeOutput,
                                markedTimeInput
                        )
                );
    }

    private void processInterval(
            MarkedTime markedTime,
            List<ExtraHourDTO> extraHourDTOS,
            Interval interval,
            LocalTime markedTimeOutput,
            LocalTime markedTimeInput
    ) {
        final LocalTime intervalInput = interval.getInput();
        final LocalTime intervalOutput = interval.getOutput();

        if ((markedTime.getInput().isBefore(intervalInput) || markedTime.getInput().equals(intervalInput))
                && (markedTimeOutput.isAfter(intervalOutput) || markedTimeOutput.equals(intervalOutput))) {
            extraHourDTOS.add(new ExtraHourDTO(intervalInput, intervalOutput));
        } else if ((markedTimeInput.isBefore(intervalInput) || markedTime.getInput().equals(intervalInput))
                && markedTimeOutput.isAfter(intervalInput) && markedTimeOutput.isBefore(intervalOutput)) {
            extraHourDTOS.add(new ExtraHourDTO(intervalInput, markedTimeOutput));
        }
        if (markedTimeInput.isAfter(intervalInput) && markedTimeInput.isBefore(intervalOutput) &&
                (markedTimeOutput.isAfter(intervalOutput) || markedTimeOutput.equals(intervalOutput))) {
            extraHourDTOS.add(new ExtraHourDTO(markedTimeInput, intervalOutput));
        }
        if (markedTimeInput.isAfter(intervalInput) && markedTime.getInput().isBefore(intervalOutput)
                && markedTimeOutput.isBefore(intervalOutput) && markedTimeOutput.isAfter(intervalInput)) {
            extraHourDTOS.add(new ExtraHourDTO(markedTimeInput, intervalOutput));
        }
    }
}
