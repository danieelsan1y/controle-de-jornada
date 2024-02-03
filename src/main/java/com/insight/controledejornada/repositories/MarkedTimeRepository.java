package com.insight.controledejornada.repositories;

import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;

import java.util.List;
import java.util.Optional;

public interface MarkedTimeRepository {

    void insert(MarkedTime entity);

    MarkedTime update(MarkedTime entity);

    void delete(long id);

    void deleteAll();

    Optional<MarkedTime> findById(Long id);

    List<MarkedTime> listAll();

    long getNextId();
}
