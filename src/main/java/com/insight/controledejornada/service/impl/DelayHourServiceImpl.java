package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.DelayHourDTO;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.Time;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.DelayHourService;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DelayHourServiceImpl implements DelayHourService {

    private final WorkTimeRepository workTimeRepository;

    private final MarkedTimeRepository markedTimeRepository;

    @Override
    public List<DelayHourDTO> getDelayHours() {
        final List<WorkTime> workTimes = workTimeRepository.listAll();

        final List<MarkedTime> markedTimes = markedTimeRepository.listAll();

        final List<DelayHourDTO> delayHourDTOS = new ArrayList<>(0);
        for (WorkTime workTime : workTimes) {
            this.processSpansToNextDay(workTime, markedTimes, delayHourDTOS);
            this.processNotSpansToNextDay(workTime, markedTimes, delayHourDTOS);
        }

        return delayHourDTOS;
    }

    private Optional<MarkedTime> getUnion(WorkTime workTime, List<MarkedTime> list) {
        return list.stream()
                .filter(Time::spansToNextDay)
                .filter(it ->
                        workTime.getInput().isBefore(it.getInput()) &&
                                workTime.getOutput().isAfter(it.getOutput())
                )
                .collect(Collectors.toList())
                .stream()
                .findFirst();
    }

    private void processNotSpansToNextDay(
            WorkTime workTime,
            List<MarkedTime> markedTimes,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (workTime.spansToNextDay()) {
            return;
        }

        final List<MarkedTime> markingsInThePeriod = this.getMarkingsInThePeriodNotSpansToNextDay(workTime, markedTimes);

        if (!markingsInThePeriod.isEmpty()) {
            this.setMarkingsFoundNotSpansToNextDay(workTime, delayHourDTOS, markingsInThePeriod);
        } else {
            this.setMarkingsNotFoundNotSpansToNextDay(workTime, markedTimes, delayHourDTOS);
        }
    }

    private List<MarkedTime> getMarkingsInThePeriodNotSpansToNextDay(
            WorkTime workTime,
            List<MarkedTime> markedTimes
    ) {
        final LocalTime workTimeInput = workTime.getInput();
        final LocalTime workTimeOutput = workTime.getOutput();

        return markedTimes.stream()
                .filter(it -> !it.spansToNextDay())
                .filter(it -> (
                                workTimeOutput.compareTo(it.getOutput()) > -1
                                        || (workTimeInput.isBefore(it.getInput()) && workTimeOutput.isAfter(it.getInput()))
                        )
                                && workTimeInput.isBefore(it.getOutput())
                )
                .collect(Collectors.toList());
    }

    private void setMarkingsFoundNotSpansToNextDay(
            WorkTime workTime,
            List<DelayHourDTO> delayHourDTOS,
            List<MarkedTime> markedTeste
    ) {
        if (markedTeste.size() == 1) {
            this.processInputAndOutputNotSpansToNextDay(workTime, delayHourDTOS, markedTeste.get(0), markedTeste.get(0));
        } else {
            this.processInputAndOutputNotSpansToNextDay(
                    workTime, delayHourDTOS,
                    markedTeste.get(0),
                    markedTeste.get(markedTeste.size() - 1)
            );

            this.processIntervals(markedTeste, delayHourDTOS);
        }
    }

    private void setMarkingsNotFoundNotSpansToNextDay(
            WorkTime workTime,
            List<MarkedTime> markedTimes,
            List<DelayHourDTO> delayHourDTOS
    ) {
        final Optional<MarkedTime> equals = this.getEquals(workTime, markedTimes)
                .filter(Time::notSpansToNextDay);

        final Optional<MarkedTime> greater = this.getGreater(workTime, markedTimes)
                .filter(Time::notSpansToNextDay);

        final Optional<MarkedTime> union = this.getUnion(workTime, markedTimes);
        boolean isWorkingHours = true;

        if (union.isPresent()) {
            isWorkingHours = workTimeRepository.listAll()
                    .stream()
                    .anyMatch(it ->
                            it.getInput().equals(union.get().getInput()) &&
                                    it.getOutput().equals(union.get().getOutput())
                    );
        }

        if (equals.isEmpty() && greater.isEmpty() && isWorkingHours) {
            delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
        }
        if (!isWorkingHours) {
            delayHourDTOS.add(new DelayHourDTO(union.get().getOutput(), workTime.getOutput()));
        }
    }

    private void processInputAndOutputNotSpansToNextDay(
            WorkTime workTime,
            List<DelayHourDTO> delayHourDTOS,
            MarkedTime first,
            MarkedTime last
    ) {
        if (first.getInput().isAfter(workTime.getInput())) {
            delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), first.getInput()));
        }
        if (last.getOutput().isBefore(workTime.getOutput())) {
            delayHourDTOS.add(new DelayHourDTO(last.getOutput(), workTime.getOutput()));
        }
    }

    private void processSpansToNextDay(
            WorkTime workTime,
            List<MarkedTime> markedTimes,
            List<DelayHourDTO> delayHourDTOS
    ) {
        if (workTime.notSpansToNextDay()) {
            return;
        }

        final List<MarkedTime> sortedTimes = new ArrayList<>(markedTimes.size());
        sortedTimes.addAll(markedTimes.stream().filter(Time::spansToNextDay).collect(Collectors.toList()));
        sortedTimes.addAll(markedTimes.stream().filter(Time::notSpansToNextDay).collect(Collectors.toList()));

        final List<MarkedTime> markingsInThePeriod = this.getMarkingsInThePeriod(sortedTimes, workTime);

        if (!markingsInThePeriod.isEmpty()) {
            if (markingsInThePeriod.size() == 1) {
                MarkedTime first = markingsInThePeriod.get(0);
                this.processInputAndOutputSpansToNextDay(delayHourDTOS, first, first, workTime);
            } else {
                this.processInputAndOutputSpansToNextDay(
                        delayHourDTOS,
                        markingsInThePeriod.get(0),
                        markingsInThePeriod.get(markedTimes.size() - 1),
                        workTime
                );

                this.processIntervals(markingsInThePeriod, delayHourDTOS);
            }
        } else {
            final Optional<MarkedTime> equals = getEquals(workTime, sortedTimes);

            final Optional<MarkedTime> greater = getGreater(workTime, sortedTimes);

            if (equals.isEmpty() && greater.isEmpty()) {
                delayHourDTOS.add(new DelayHourDTO(workTime.getInput(), workTime.getOutput()));
            }
        }
    }

    private Optional<MarkedTime> getGreater(WorkTime workTime, List<MarkedTime> sortedTimes) {
        return sortedTimes.stream()
                .filter(it -> it.getInput().compareTo(workTime.getInput()) < 1
                        && it.getOutput().compareTo(workTime.getOutput()) > -1
                )
                .filter(it -> !it.spansToNextDay())
                .findFirst();
    }

    private Optional<MarkedTime> getEquals(WorkTime workTime, List<MarkedTime> sortedTimes) {
        return sortedTimes.stream()
                .filter(it -> it.getInput().equals(workTime.getInput())
                        && it.getOutput().equals(workTime.getOutput())
                )
                .findFirst();
    }

    private List<MarkedTime> getMarkingsInThePeriod(List<MarkedTime> sortedTimes, WorkTime workTime) {
        final LocalTime workTimeOutput = workTime.getOutput();
        final List<MarkedTime> markingsInThePeriod = new ArrayList<>(0);
        sortedTimes.forEach(it -> {
            if (it.spansToNextDay()) {
                markingsInThePeriod.add(new MarkedTime(it.getId(), it.getInput(), it.getOutput()));
            } else if (it.getInput().compareTo(workTimeOutput) < 1 || it.getOutput().compareTo(workTimeOutput) < 1) {
                markingsInThePeriod.add(new MarkedTime(it.getId(), it.getInput(), it.getOutput()));
            } else if (it.getOutput().equals(workTime.getInput())) {
                markingsInThePeriod.add(new MarkedTime(it.getId(), it.getInput(), it.getOutput()));
            }
        });

        return markingsInThePeriod;
    }

    private void processInputAndOutputSpansToNextDay(
            List<DelayHourDTO> delayHourDTOS,
            MarkedTime first,
            MarkedTime last,
            WorkTime workTime
    ) {
        final LocalTime workTimeInput = workTime.getInput();
        final LocalTime workTimeOutput = workTime.getOutput();

        if (first.getInput().isBefore(workTimeInput) && first.getInput().isBefore(workTimeOutput)) {
            delayHourDTOS.add(new DelayHourDTO(workTimeInput, first.getInput()));
        } else if (first.getInput().isAfter(workTimeInput)) {
            delayHourDTOS.add(new DelayHourDTO(workTimeInput, first.getInput()));
        }
        if (last.getOutput().isBefore(workTimeOutput) || (last.getOutput().isAfter(workTimeInput) && last.getOutput().isAfter(workTimeOutput))) {
            delayHourDTOS.add(new DelayHourDTO(first.getOutput(), workTimeOutput));
        }
    }

    private void processIntervals(
            List<MarkedTime> markedTimes,
            List<DelayHourDTO> delayHourDTOS
    ) {

        for (int i = 0; i < markedTimes.size() - 1; i++) {
            delayHourDTOS.add(new DelayHourDTO(markedTimes.get(i).getOutput(), markedTimes.get(i + 1).getInput()));
        }
    }
}
