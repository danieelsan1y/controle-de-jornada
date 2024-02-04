package com.insight.controledejornada.service;

import com.insight.controledejornada.dto.MarkedTimeDTO;

import java.util.List;

public interface MarkedTimeService {

    void insert(MarkedTimeDTO dto);

    MarkedTimeDTO update(MarkedTimeDTO dto);

    void delete(String id);

    void deleteAll();

    MarkedTimeDTO findById(Long id);

    List<MarkedTimeDTO> listAll();
}
