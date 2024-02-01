package com.insight.controledejornada.service;

import com.insight.controledejornada.model.ExtraHour;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.impl.ExtraHourServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class ExtraHourServiceTest {

    private WorkTimeRepository workTimeRepository;

    private MarkedTimeRepository markedTimeRepository;

    private ExtraHourService extraHourService;

    @BeforeEach
    public void setup() {
        this.workTimeRepository = Mockito.mock(WorkTimeRepositoryImpl.class);
        this.markedTimeRepository = Mockito.mock(MarkedTimeRepository.class);
        this.extraHourService = new ExtraHourServiceImpl(this.workTimeRepository, this.markedTimeRepository);
    }

    @Test
    public void exampleOfTestTwoWithOvertimeFromSevenOClockToEightOClock() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleOneAndTwo());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getRecordsExampleOneAndTwo());

        final List<ExtraHour> extraHours = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHours.size());

        Assertions.assertEquals(extraHours.stream().findFirst().get().getStart(), LocalTime.of(7, 0));
        Assertions.assertEquals(extraHours.stream().findFirst().get().getEnd(), LocalTime.of(8, 0));
    }

    @Test
    public void ExampleOfTestThreeWithOvertimeFromSixOClockToEightOClockFromTwelveToThirteenThirtyAndFromSeventeenThirtyToTwenty() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleThree());

        final List<ExtraHour> extraHours = this.extraHourService.getExtraHours();

        Assertions.assertEquals(3, extraHours.size());

        Assertions.assertEquals(extraHours.get(0).getStart(), LocalTime.of(6, 0));
        Assertions.assertEquals(extraHours.get(0).getEnd(), LocalTime.of(8, 0));

        Assertions.assertEquals(extraHours.get(1).getStart(), LocalTime.of(12, 0));
        Assertions.assertEquals(extraHours.get(1).getEnd(), LocalTime.of(13, 30));

        Assertions.assertEquals(extraHours.get(2).getStart(), LocalTime.of(17, 30));
        Assertions.assertEquals(extraHours.get(2).getEnd(), LocalTime.of(20, 0));
    }

    @Test
    public void test() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleThree());

        final List<ExtraHour> extraHours = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHours.size());

        Assertions.assertEquals(extraHours.get(0).getStart(), LocalTime.of(7, 0));
        Assertions.assertEquals(extraHours.get(0).getEnd(), LocalTime.of(8, 0));

        Assertions.assertEquals(extraHours.get(1).getStart(), LocalTime.of(12, 0));
        Assertions.assertEquals(extraHours.get(1).getEnd(), LocalTime.of(12, 30));
    }

    @Test
    public void test2() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleFour());

        final List<ExtraHour> extraHours = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHours.size());

        Assertions.assertEquals(extraHours.get(0).getStart(), LocalTime.of(21, 0));
        Assertions.assertEquals(extraHours.get(0).getEnd(), LocalTime.of(22, 0));
    }

    @Test
    public void test3() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleFour());

        final List<ExtraHour> extraHours = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHours.size());

        Assertions.assertEquals(extraHours.get(0).getStart(), LocalTime.of(5, 0));
        Assertions.assertEquals(extraHours.get(0).getEnd(), LocalTime.of(7, 0));
    }

    private List<WorkTime> getWorkScheduleExampleOneAndTwo() {
        return List.of(new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)));
    }

    private List<MarkedTime> getRecordsExampleOneAndTwo() {
        return List.of(new MarkedTime(1L, LocalTime.of(7, 0), LocalTime.of(11, 0)));
    }

    private List<WorkTime> getWorkScheduleExampleThree() {
        return List.of(
                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                new WorkTime(2L, LocalTime.of(13, 30), LocalTime.of(17, 30))
        );
    }

    private List<MarkedTime> getFirstRecordsExampleThree() {
        return List.of(new MarkedTime(1L, LocalTime.of(6, 0), LocalTime.of(20, 0)));
    }

    private List<MarkedTime> getSeccondRecordsExampleThree() {
        return List.of(
                new MarkedTime(1L, LocalTime.of(7, 0), LocalTime.of(12, 30)),
                new MarkedTime(2L, LocalTime.of(14, 0), LocalTime.of(17, 30))
        );
    }

    private List<WorkTime> getWorkScheduleExampleFour() {
        return List.of(
                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(5, 0))
        );
    }

    private List<MarkedTime> getFirstRecordsExampleFour() {
        return List.of(new MarkedTime(1L, LocalTime.of(21, 0), LocalTime.of(4, 0)));
    }

    private List<MarkedTime> getSeccondRecordsExampleFour() {
        return List.of(new MarkedTime(1L, LocalTime.of(3, 0), LocalTime.of(7, 0)));
    }
}