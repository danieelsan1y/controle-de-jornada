package com.insight.controledejornada.service;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.dto.WorkTimeDTO;

import java.util.List;

public interface MarkedTimeService {
    void insert(MarkedTimeDTO dto);

    MarkedTimeDTO update(MarkedTimeDTO dto);

    void delete(MarkedTimeDTO dto);

    void deleteAll();

    MarkedTimeDTO findById(Long id);

    List<MarkedTimeDTO> listAll();

}
