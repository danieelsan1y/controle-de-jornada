package com.insight.controledejornada.service;

import com.insight.controledejornada.dto.ExtraHourDTO;
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
class ExtraHourDTOServiceTest {

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
    public void testExampleTwoWithOvertimeFromSevenToEight() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleOneAndTwo());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getRecordsExampleOneAndTwo());

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(8, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void testExampleThreeWithOvertimeFromSixToEightAndTwelveToThirteenThirtyAndSeventeenThirtyToTwenty() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleThree());

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(3, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(6, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(8, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(12, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(13, 30), extraHourDTOS.get(1).getEnd());

        Assertions.assertEquals(LocalTime.of(17, 30), extraHourDTOS.get(2).getStart());
        Assertions.assertEquals(LocalTime.of(20, 0), extraHourDTOS.get(2).getEnd());
    }

    @Test
    public void testExample3OvertimeReturningSevenToEightAndTwelveToTwelveThirty() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleThree());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleThree());

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(8, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(12, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(12, 30), extraHourDTOS.get(1).getEnd());
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
                        List.of(
                                new MarkedTime(1L, LocalTime.of(8, 0), LocalTime.of(9, 30)),
                                new MarkedTime(2L, LocalTime.of(10, 30), LocalTime.of(12, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(0, extraHourDTOS.size());
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
                        List.of(
                                new MarkedTime(1L, LocalTime.of(6, 0), LocalTime.of(7, 30)),
                                new MarkedTime(2L, LocalTime.of(8, 15), LocalTime.of(10, 0)),
                                new MarkedTime(3L, LocalTime.of(10, 10), LocalTime.of(11, 35)),
                                new MarkedTime(4L, LocalTime.of(11, 50), LocalTime.of(12, 45))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(6, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(7, 30), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(12, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(12, 45), extraHourDTOS.get(1).getEnd());
    }

    @Test
    public void test3() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new WorkTime(1L, LocalTime.of(14, 0), LocalTime.of(18, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                                new MarkedTime(2L, LocalTime.of(15, 0), LocalTime.of(19, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(12, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(13, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(18, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(19, 0), extraHourDTOS.get(1).getEnd());
    }

    @Test
    public void test4() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new WorkTime(1L, LocalTime.of(14, 0), LocalTime.of(18, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new MarkedTime(2L, LocalTime.of(19, 0), LocalTime.of(21, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(19, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(21, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void test5() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new WorkTime(1L, LocalTime.of(14, 0), LocalTime.of(18, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(8, 0), LocalTime.of(11, 0)),
                                new MarkedTime(2L, LocalTime.of(12, 0), LocalTime.of(16, 0)),
                                new MarkedTime(3L, LocalTime.of(17, 0), LocalTime.of(21, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(12, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(14, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(18, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(21, 0), extraHourDTOS.get(1).getEnd());
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
                        List.of(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(0, extraHourDTOS.size());
    }

    @Test
    public void test7() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(20, 0), LocalTime.of(22, 0)),
                                new MarkedTime(2L, LocalTime.of(6, 0), LocalTime.of(7, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(20, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(22, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(6, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(1).getEnd());
    }

    //voltar
    @Test
    public void test8() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(23, 0), LocalTime.of(2, 0)),
                                new MarkedTime(2L, LocalTime.of(3, 0), LocalTime.of(7, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(1, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(2, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(6, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(1).getEnd());
    }

    @Test
    public void test9() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(7, 0), LocalTime.of(15, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(15, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void test10() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(5, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(1, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(2, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void test11() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new MarkedTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0)),
                                new MarkedTime(2L, LocalTime.of(7, 0), LocalTime.of(10, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(10, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void test12() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new WorkTime(1L, LocalTime.of(22, 0), LocalTime.of(1, 0)),
                                new WorkTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0))
                        )
                );

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(
                        List.of(
                                new MarkedTime(1L, LocalTime.of(20, 0), LocalTime.of(22, 0)),
                                new MarkedTime(2L, LocalTime.of(2, 0), LocalTime.of(6, 0)),
                                new MarkedTime(2L, LocalTime.of(7, 0), LocalTime.of(10, 0))
                        )
                );

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(2, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(20, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(22, 0), extraHourDTOS.get(0).getEnd());

        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(1).getStart());
        Assertions.assertEquals(LocalTime.of(10, 0), extraHourDTOS.get(1).getEnd());
    }

    @Test
    public void testExample4OvertimeReturningTwentyOneToTwentyTwo() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getFirstRecordsExampleFour());

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(21, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(22, 0), extraHourDTOS.get(0).getEnd());
    }

    @Test
    public void testExample4OvertimeReturningFromFiveToSeven() {
        Mockito.when(this.workTimeRepository.listAll())
                .thenReturn(getWorkScheduleExampleFour());

        Mockito.when(this.markedTimeRepository.listAll())
                .thenReturn(getSeccondRecordsExampleFour());

        final List<ExtraHourDTO> extraHourDTOS = this.extraHourService.getExtraHours();

        Assertions.assertEquals(1, extraHourDTOS.size());

        Assertions.assertEquals(LocalTime.of(5, 0), extraHourDTOS.get(0).getStart());
        Assertions.assertEquals(LocalTime.of(7, 0), extraHourDTOS.get(0).getEnd());
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