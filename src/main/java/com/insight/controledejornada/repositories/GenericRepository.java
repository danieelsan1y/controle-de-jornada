package com.insight.controledejornada.repositories;

import com.insight.controledejornada.model.Time;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GenericRepository<T extends Time> {

    public void insert(T time, List<T> times) {
        time.setId(this.getNextId(times));
        times.add(time);
    }

    public T update(T time, List<T> times) {
        final AtomicInteger i = new AtomicInteger();
        times.forEach(te -> {
            if (te.getId().equals(time.getId())) {
                times.set(i.get(), time);
            }
            i.getAndIncrement();
        });

        return time;
    }

    public void delete(long id, List<T> times) {
        times.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .ifPresent(times::remove);
    }

    public void deleteAll(List<T> times) {
        times.clear();
    }

    public Optional<T> findById(Long id, List<T> times) {
        return times.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    public List<T> listAll(List<T> times) {
        return times;
    }

    public long getNextId(List<T> times) {
        return times.size() + 1;
    }
}

