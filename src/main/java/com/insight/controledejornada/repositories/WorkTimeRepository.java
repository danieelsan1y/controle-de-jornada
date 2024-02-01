package com.insight.controledejornada.repositories;

import com.insight.controledejornada.model.WorkTime;

import java.util.List;
import java.util.Optional;

public interface WorkTimeRepository {

    void insert(WorkTime entity);

    WorkTime update(WorkTime entity);

    void delete(WorkTime entity);

    void deleteAll();

    Optional<WorkTime> findById(Long id);

    List<WorkTime> listAll();

    long getNextId();
}
