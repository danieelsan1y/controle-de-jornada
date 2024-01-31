package com.insight.controledejornada;

import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;

import java.time.LocalTime;

public class Application {
    public static void main(String[] args) {
        WorkTimeRepository workTimeRepository = new WorkTimeRepositoryImpl();

        WorkTime workTime = new WorkTime(LocalTime.now(),LocalTime.now());

        workTimeRepository.insert(workTime);
        workTimeRepository.insert(new WorkTime(LocalTime.now(), LocalTime.now()));

        System.out.println(workTimeRepository.findById(workTime.getId()).get().getInput());
    }
}
