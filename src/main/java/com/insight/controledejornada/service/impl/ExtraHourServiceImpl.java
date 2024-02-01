package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.model.*;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.ExtraHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExtraHourServiceImpl implements ExtraHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    public List<ExtraHour> getExtraHours() {
        final List<WorkTime> workTimes = this.workTimeRepository.listAll();
        final List<MarkedTime> markedTimes = this.markedTimeRepository.listAll();
        final List<ExtraHour> extraHours = new ArrayList<>(0);
        final List<Interval> intervals = getIntervals(workTimes);

        final WorkTime first = workTimes.stream().findFirst().orElseThrow(RuntimeException::new);
        final WorkTime last = workTimes.get(workTimes.size() - 1);

        this.setExtraHours(markedTimes, first, extraHours, intervals, last);

        return extraHours.stream()
                .sorted(Comparator.comparing(ExtraHour::getStart))
                .collect(Collectors.toList());
    }

    private void setExtraHours(
            List<MarkedTime> markedTimes,
            WorkTime first,
            List<ExtraHour> extraHours,
            List<Interval> intervals,
            WorkTime last
    ) {
        markedTimes
                .stream()
                .filter(Objects::nonNull)
                .forEach(it -> {
                    if (it.getInput().isBefore(first.getInput())) {
                        if (first.spansToNextDay() && it.getInput().isAfter(first.getOutput())) {
                            extraHours.add(new ExtraHour(it.getInput(), first.getInput()));
                        } else if (!first.spansToNextDay()) {
                            extraHours.add(new ExtraHour(it.getInput(), first.getInput()));
                        }
                    }

                    this.setExtraTimeBy(it, intervals, extraHours);

                    if (last != null && it.getOutput().isAfter(last.getOutput())) {
                        extraHours.add(new ExtraHour(last.getOutput(), it.getOutput()));
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

    private void setExtraTimeBy(MarkedTime it, List<Interval> intervals, List<ExtraHour> extraHours) {
        final LocalTime markedTimeInput = it.getInput();
        final LocalTime markedTimeOutput = it.getOutput();
        intervals.stream()
                .filter(Objects::nonNull)
                .forEach(interval ->
                        this.processInterval(
                                it,
                                extraHours,
                                interval,
                                markedTimeOutput,
                                markedTimeInput
                        )
                );
    }

    private void processInterval(
            MarkedTime markedTime,
            List<ExtraHour> extraHours,
            Interval interval,
            LocalTime markedTimeOutput,
            LocalTime markedTimeInput
    ) {
        final LocalTime intervalInput = interval.getInput();
        final LocalTime intervalOutput = interval.getOutput();

        if ((markedTime.getInput().isBefore(intervalInput) || markedTime.getInput().equals(intervalInput))
                && (markedTimeOutput.isAfter(intervalOutput) || markedTimeOutput.equals(intervalOutput))) {
            extraHours.add(new ExtraHour(intervalInput, intervalOutput));
        } else if ((markedTimeInput.isBefore(intervalInput) || markedTime.getInput().equals(intervalInput))
                && markedTimeOutput.isAfter(intervalInput) && markedTimeOutput.isBefore(intervalOutput)) {
            extraHours.add(new ExtraHour(intervalInput, markedTimeOutput));
        }
        if (markedTimeInput.isAfter(intervalInput) && markedTimeInput.isBefore(intervalOutput) &&
                (markedTimeOutput.isAfter(intervalOutput) || markedTimeOutput.equals(intervalOutput))) {
            extraHours.add(new ExtraHour(markedTimeInput, intervalOutput));
        }
        if (markedTimeInput.isAfter(intervalInput) && markedTime.getInput().isBefore(intervalOutput)
                && markedTimeOutput.isBefore(intervalOutput) && markedTimeOutput.isAfter(intervalInput)) {
            extraHours.add(new ExtraHour(markedTimeInput, intervalOutput));
        }
    }
}
