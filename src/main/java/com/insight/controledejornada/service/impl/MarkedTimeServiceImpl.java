package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.repositories.impl.MakedTimeRepositoryImpl;
import com.insight.controledejornada.service.MarkedTimeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MarkedTimeServiceImpl implements MarkedTimeService {

    final MarkedTimeRepository markedTimeRepository;

    @Override
    public void insert(MarkedTimeDTO dto) {
        final MarkedTime markedTime = this.convertTo(dto);
        this.markedTimeRepository.insert(markedTime);
    }

    @Override
    public MarkedTimeDTO update(MarkedTimeDTO dto) {
        this.validate(dto);
        final MarkedTime markedTime = this.convertTo(dto);

        return Optional.ofNullable(this.markedTimeRepository.findById(markedTime.getId()))
                .map(it -> this.markedTimeRepository.update(markedTime))
                .map(it -> new MarkedTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException("Não foi possível atualizar"));
    }

    @Override
    public void delete(String id) {
        long idFormatted = Optional.ofNullable(id)
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .orElseThrow(() -> new SystemException("id é requerido para remoção."));

        this.markedTimeRepository.delete(idFormatted);
    }

    @Override
    public void deleteAll() {
        this.markedTimeRepository.deleteAll();
    }

    @Override
    public MarkedTimeDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new SystemException("id é requerido");
        }

        return this.markedTimeRepository.findById(id)
                .map(it -> new MarkedTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException("o Objeto 'workTime' não foi encontrado"));
    }

    @Override
    public List<MarkedTimeDTO> listAll() {
        return this.markedTimeRepository.listAll()
                .stream()
                .map(it -> new MarkedTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .collect(Collectors.toList());
    }

    private void validate(MarkedTimeDTO dto) {
        if (dto == null) {
            throw new SystemException("O Objeto 'workTime' não pode ser nulo");
        }
    }

    private MarkedTime convertTo(MarkedTimeDTO dto) {
        return Optional.ofNullable(dto)
                .map(MarkedTime::new)
                .orElseThrow(RuntimeException::new);
    }
}
