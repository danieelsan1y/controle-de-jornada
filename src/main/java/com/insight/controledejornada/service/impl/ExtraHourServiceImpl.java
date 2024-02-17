package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.ExtraHourDTO;
import com.insight.controledejornada.model.Interval;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.ExtraHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ExtraHourServiceImpl implements ExtraHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    public List<ExtraHourDTO> getExtraHours() {
        final List<WorkTime> workTimes = workTimeRepository.listAll();
        final List<MarkedTime> markedTimes = markedTimeRepository.listAll();

        if (workTimes.isEmpty() || markedTimes.isEmpty()) {
            return new ArrayList<>(0);
        }

        final List<ExtraHourDTO> extraHourDTOS = new ArrayList<>(0);
        final List<Interval> intervals = getIntervals(workTimes);

        final WorkTime first = workTimes.stream().findFirst().orElseThrow(RuntimeException::new);
        final WorkTime last = workTimes.get(workTimes.size() - 1);

        this.setExtraHours(markedTimes, first, extraHourDTOS, intervals, last);

        return extraHourDTOS;
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
                    if (first.notSpansToNextDay() && it.notSpansToNextDay()) {
                        this.setOvertimeBeforeWork(first, extraHourDTOS, it);
                        this.setOvertimeDuringTheBreak(it, intervals, extraHourDTOS);
                        this.setOvertimeAfterWork(extraHourDTOS, last, it);
                    } else {
                        this.processSpansToNexDay(first, last, extraHourDTOS, it);
                        this.setOvertimeDuringTheBreak(it, intervals, extraHourDTOS);
                    }
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
            if (markedTime.getOutput().isBefore(first.getInput())) {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), markedTime.getOutput()));
            } else {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), first.getInput()));
            }
        }
    }

    private void processSpansToNexDay(
            WorkTime first,
            WorkTime last,
            List<ExtraHourDTO> extraHourDTOS,
            MarkedTime markedTime
    ) {
        if (markedTime.getOutput().compareTo(first.getInput()) < 1
                && markedTime.getOutput().isAfter(first.getOutput())
                && markedTime.getInput().compareTo(last.getOutput()) > -1
                && first.getId().equals(last.getId())
        ) {
            extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), markedTime.getOutput()));
        } else {
            if (markedTime.getInput().isBefore(first.getInput()) && markedTime.getInput().isAfter(first.getOutput())) {
                if (markedTime.getOutput().isBefore(first.getOutput())) {
                    extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), first.getInput()));
                }
            }
            if (markedTime.getInput().isBefore(last.getOutput()) && markedTime.getOutput().isAfter(last.getOutput())) {
                extraHourDTOS.add(new ExtraHourDTO(last.getOutput(), markedTime.getOutput()));
            } else if (markedTime.getInput().compareTo(last.getOutput()) > -1 && markedTime.getOutput().isAfter(last.getOutput())) {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), markedTime.getOutput()));
            }
        }
    }

    private void setOvertimeAfterWork(
            List<ExtraHourDTO> extraHourDTOS,
            WorkTime last,
            MarkedTime markedTime
    ) {
        if (last != null) {
            if (markedTime.getOutput().isAfter(last.getOutput()) && markedTime.getInput().isAfter(last.getOutput())) {
                extraHourDTOS.add(new ExtraHourDTO(markedTime.getInput(), markedTime.getOutput()));
            } else if (markedTime.getOutput().isAfter(last.getOutput())) {
                extraHourDTOS.add(new ExtraHourDTO(last.getOutput(), markedTime.getOutput()));
            }
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
        } else if (markedTimeInput.isAfter(intervalInput) && markedTimeInput.isBefore(intervalOutput) &&
                (markedTimeOutput.isAfter(intervalOutput) || markedTimeOutput.equals(intervalOutput))) {
            extraHourDTOS.add(new ExtraHourDTO(markedTimeInput, intervalOutput));
        } else if (markedTimeInput.isAfter(intervalInput) && markedTime.getInput().isBefore(intervalOutput)
                && markedTimeOutput.isBefore(intervalOutput) && markedTimeOutput.isAfter(intervalInput)) {
            extraHourDTOS.add(new ExtraHourDTO(markedTimeInput, intervalOutput));
        } else if (markedTime.getOutput().compareTo(intervalInput) > -1
                && markedTime.spansToNextDay()
                && markedTime.getOutput().compareTo(intervalOutput) > -1
        ) {
            extraHourDTOS.add(new ExtraHourDTO(intervalInput, intervalOutput));
        }
    }
}
