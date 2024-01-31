package com.insight.controledejornada.repositories.impl;

import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.repositories.GenericRepository;
import com.insight.controledejornada.repositories.MarkedTimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakedTimeRepositoryImpl extends GenericRepository<MarkedTime> implements MarkedTimeRepository {

    private static final List<MarkedTime> markedTimes = new ArrayList<>();


    public void insert(MarkedTime entity) {
        super.insert(entity, markedTimes);
    }


    public MarkedTime update(MarkedTime entity) {
        return super.update(entity, markedTimes);
    }


    public void delete(MarkedTime entity) {
        super.delete(entity, markedTimes);
    }


    public void deleteAll() {
        super.deleteAll(markedTimes);
    }


    public Optional<MarkedTime> findById(Long id) {
        return super.findById(id, markedTimes);
    }

    public List<MarkedTime> listAll() {
        return super.listAll(markedTimes);
    }

    @Override
    public long getNextId() {
        return super.getNextId(markedTimes);
    }

}