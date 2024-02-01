package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.WorkTimeService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    @Override
    public void insert(WorkTimeDTO dto) {
        final WorkTime workTime = this.convertTo(dto);

        if (this.workTimeRepository.getNextId() > 3) {
            throw new SystemException("É Possível cadastrar apenas 3 Horários de trabalho");
        }

        this.workTimeRepository.insert(workTime);
    }

    @Override
    public WorkTimeDTO update(WorkTimeDTO dto) {
        this.validate(dto);
        final WorkTime workTime = this.convertTo(dto);

        return Optional.ofNullable(this.workTimeRepository.findById(workTime.getId()))
                .map(it -> this.workTimeRepository.update(workTime))
                .map(it -> new WorkTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException("Não foi possível atualizar"));
    }

    @Override
    public void delete(WorkTimeDTO dto) {
        this.validate(dto);

        final WorkTime workTime = this.convertTo(dto);

        this.workTimeRepository.delete(workTime);
    }

    @Override
    public void deleteAll() {
        workTimeRepository.deleteAll();
    }

    @Override
    public WorkTimeDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new SystemException("id é requerido");
        }

        return this.workTimeRepository.findById(id)
                .map(it -> new WorkTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException("o Objeto 'workTime' não foi encontrado"));
    }

    @Override
    public List<WorkTimeDTO> listAll() {
        return this.workTimeRepository.listAll()
                .stream()
                .map(it -> new WorkTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .collect(Collectors.toList());
    }

    private void validate(WorkTimeDTO dto) {
        if (dto == null) {
            throw new SystemException("O Objeto 'workTime' não pode ser nulo");
        }
    }

    private WorkTime convertTo(WorkTimeDTO dto) {
        return Optional.ofNullable(dto)
                .map(WorkTime::new)
                .orElseThrow(RuntimeException::new);
    }
}
