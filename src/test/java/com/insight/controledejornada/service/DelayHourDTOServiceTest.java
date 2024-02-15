package com.insight.controledejornada.service;

import com.insight.controledejornada.dto.DelayHourDTO;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.impl.DelayHourServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class DelayHourDTOServiceTest {

    private WorkTimeRepository workTimeRepository;

    private MarkedTimeRepository markedTimeRepository;

    private DelayHourService delayHourService;

    @BeforeEach
    public void setup() {
        this.workTimeRepository = Mockito.mock(WorkTimeRepositoryImpl.class);
        this.markedTimeRepository = Mockito.mock(MarkedTimeRepository.class);
        this.delayHourService = new DelayHourServiceImpl(this.workTimeRepository, this.markedTimeRepository);
    }

    @Test
    public void testDelayFrom11To12() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleOneAndTwo());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getRecordsExampleOneAndTwo());

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(11, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(12, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void testNoDelays() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleThree());

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(0, delayHourDTOS.size());
    }

    @Test
    public void testDelaysDuringWorkHours13h30To14h00And17h00To17h30() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleThree());

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(2, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(13, 30), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(14, 0), delayHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(17, 0), delayHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(17, 30), delayHourDTOS.get(1).getEnd());
    }

    @Test
    public void testDelaysDuringWorkHours04h00To05h00() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleFour());

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(4, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(5, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void testDelaysDuringWorkHours22h00To03h00() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleFour());

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(22, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(3, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void test1() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(8, 0), LocalTime.of(9, 30)),
                                new MarkedTime(2L, LocalTime.of(10, 30), LocalTime.of(12, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(9, 30), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(10, 30), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void test2() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(6, 0), LocalTime.of(7, 30)),
                                new MarkedTime(2L, LocalTime.of(8, 15), LocalTime.of(10, 0)),
                                new MarkedTime(3L, LocalTime.of(10, 10), LocalTime.of(11, 35)),
                                new MarkedTime(4L, LocalTime.of(11, 50), LocalTime.of(12, 45))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(3, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(8, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(8, 15), delayHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(10, 0), delayHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(10, 10), delayHourDTOS.get(1).getEnd());

        Assertions.assertEquals(LocalTime.of(11, 35), delayHourDTOS.get(2).getStart());
        Assertions.assertEquals(LocalTime.of(11, 50), delayHourDTOS.get(2).getEnd());
    }

    @Test
    public void test3() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new WorkTime(2L, LocalTime.of(14, 0), LocalTime.of(18, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                                new MarkedTime(2L, LocalTime.of(15, 0), LocalTime.of(19, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(2, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(8, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(9, 0), delayHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(14, 0), delayHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(15, 0), delayHourDTOS.get(1).getEnd());
    }

    @Test
    public void test4() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new WorkTime(2L, LocalTime.of(14, 0), LocalTime.of(18, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(8, 0), LocalTime.of(11, 0)),
                                new MarkedTime(2L, LocalTime.of(12, 0), LocalTime.of(16, 0)),
                                new MarkedTime(3L, LocalTime.of(17, 0), LocalTime.of(21, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(2, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(11, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(12, 0), delayHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(16, 0), delayHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(17, 0), delayHourDTOS.get(1).getEnd());
    }

    @Test
    public void test5() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(1, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(2, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void test6() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(20, 0), LocalTime.of(22, 0)),
                                new MarkedTime(2L, LocalTime.of(6, 0), LocalTime.of(7, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(22, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(6, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void test7() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(1L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(23, 0), LocalTime.of(2, 0)),
                                new MarkedTime(2L, LocalTime.of(3, 0), LocalTime.of(7, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(2, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(22, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(23, 0), delayHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(2, 0), delayHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(3, 0), delayHourDTOS.get(1).getEnd());
    }

    @Test
    public void test8() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(1L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(7, 0), LocalTime.of(15, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(1, delayHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(2, 0), delayHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(6, 0), delayHourDTOS.get(0).getEnd());
    }

    @Test
    public void test9() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(1L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        Arrays.asList(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0)),
                                new MarkedTime(3L, LocalTime.of(7, 0), LocalTime.of(10, 0))
                        )
                );

        final List<DelayHourDTO> delayHourDTOS = this.delayHourService.getDelayHours();

        Assertions.assertEquals(0, delayHourDTOS.size());
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
                new MarkedTime(2L, LocalTime.of(14, 0), LocalTime.of(17, 0))
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