package com.insight.controledejornada.repositories.impl;

import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.GenericRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkTimeRepositoryImpl extends GenericRepository<WorkTime> implements WorkTimeRepository {

    private static final List<WorkTime> workTimes = new ArrayList<>();

    @Override
    public void insert(WorkTime entity) {
        super.insert(entity, workTimes);
    }

    @Override
    public WorkTime update(WorkTime entity) {
        return super.update(entity, workTimes);
    }

    @Override
    public void delete(long id) {
        super.delete(id, workTimes);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(workTimes);
    }

    @Override
    public Optional<WorkTime> findById(Long id) {
        return super.findById(id, workTimes);
    }

    @Override
    public List<WorkTime> listAll() {
        return super.listAll(workTimes)
                .stream()
                .sorted(Comparator.comparing(WorkTime::getInput))
                .collect(Collectors.toList());
    }

    @Override
    public long getNextId() {
        return super.getNextId(workTimes);
    }
}
