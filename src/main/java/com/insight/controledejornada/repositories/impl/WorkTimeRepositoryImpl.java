package com.insight.controledejornada.repositories.impl;

import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.GenericRepository;
import com.insight.controledejornada.repositories.WorkTimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkTimeRepositoryImpl extends GenericRepository<WorkTime> implements WorkTimeRepository {

    private static final List<WorkTime> workTimes = new ArrayList<>();


    public void insert(WorkTime entity) {
        super.insert(entity, workTimes);
    }


    public WorkTime update(WorkTime entity) {
        return super.update(entity, workTimes);
    }


    public void delete(WorkTime entity) {
        super.delete(entity, workTimes);
    }


    public void deleteAll() {
        super.deleteAll(workTimes);
    }


    public Optional<WorkTime> findById(Long id) {
        return super.findById(id, workTimes);
    }

    public List<WorkTime> listAll() {
        return super.listAll(workTimes);
    }

    @Override
    public long getNextId() {
        return super.getNextId(workTimes);
    }

}
