package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.WorkTime;
import com.insight.controledejornada.repositories.WorkTimeRepository;
import com.insight.controledejornada.service.WorkTimeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.insight.controledejornada.exception.Message.*;

@RequiredArgsConstructor
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    @Override
    public void insert(WorkTimeDTO dto) {
        final WorkTime workTime = this.convertTo(dto);

        if (this.workTimeRepository.getNextId() > 3) {
            throw new SystemException(THREE_WORK_TIME);
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
                .orElseThrow(() -> new SystemException(UNABLE_TO_UPDATE));
    }

    @Override
    public void delete(String id) {
        long idFormatted = Optional.ofNullable(id)
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .orElseThrow(() -> new SystemException(ID_REQUIRED));

        this.workTimeRepository.delete(idFormatted);
    }

    @Override
    public void deleteAll() {
        workTimeRepository.deleteAll();
    }

    @Override
    public WorkTimeDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new SystemException(ID_REQUIRED);
        }

        return this.workTimeRepository.findById(id)
                .map(it -> new WorkTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException(WORKTIME_TIME_NOT_FOUND));
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
            throw new SystemException(MARKED_TIME_NOT_NULL);
        }
    }

    private WorkTime convertTo(WorkTimeDTO dto) {
        return Optional.ofNullable(dto)
                .map(WorkTime::new)
                .orElseThrow(RuntimeException::new);
    }
}
