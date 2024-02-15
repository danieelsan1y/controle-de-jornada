package com.insight.controledejornada.repositories.impl;

import com.insight.controledejornada.dto.ExtraHourDTO;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.repositories.GenericRepository;
import com.insight.controledejornada.repositories.MarkedTimeRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MakedTimeRepositoryImpl extends GenericRepository<MarkedTime> implements MarkedTimeRepository {

    private static final List<MarkedTime> markedTimes = new ArrayList<>();

    @Override
    public void insert(MarkedTime entity) {
        super.insert(entity, markedTimes);
    }

    @Override
    public MarkedTime update(MarkedTime entity) {
        return super.update(entity, markedTimes);
    }

    @Override
    public void delete(long id) {
        super.delete(id, markedTimes);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(markedTimes);
    }

    @Override
    public Optional<MarkedTime> findById(Long id) {
        return super.findById(id, markedTimes);
    }

    @Override
    public List<MarkedTime> listAll() {
        return super.listAll(markedTimes);
    }

    @Override
    public long getNextId() {
        return super.getNextId(markedTimes);
    }
}
