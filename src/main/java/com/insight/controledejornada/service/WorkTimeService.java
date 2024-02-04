package com.insight.controledejornada.service;

import com.insight.controledejornada.dto.WorkTimeDTO;

import java.util.List;

public interface WorkTimeService {

    void insert(WorkTimeDTO dto);

    WorkTimeDTO update(WorkTimeDTO dto);

    void delete(String id);

    void deleteAll();

    WorkTimeDTO findById(Long id);

    List<WorkTimeDTO> listAll();
}
