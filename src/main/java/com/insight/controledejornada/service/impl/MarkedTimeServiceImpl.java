package com.insight.controledejornada.service.impl;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.MarkedTime;
import com.insight.controledejornada.model.Time;
import com.insight.controledejornada.repositories.MarkedTimeRepository;
import com.insight.controledejornada.service.MarkedTimeService;
import com.insight.controledejornada.utils.IntervalValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.insight.controledejornada.exception.Message.*;
import static com.insight.controledejornada.utils.IntervalValidator.validateInterval;

@RequiredArgsConstructor
public class MarkedTimeServiceImpl implements MarkedTimeService {

    final MarkedTimeRepository markedTimeRepository;

    @Override
    public void insert(MarkedTimeDTO dto) {
        final MarkedTime markedTime = this.convertTo(dto);

        validateInterval(
                markedTime,
                this.markedTimeRepository.listAll()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Time.class::cast)
                        .collect(Collectors.toList()),
                Optional.empty()
        );

        this.markedTimeRepository.insert(markedTime);
    }

    @Override
    public MarkedTimeDTO update(MarkedTimeDTO dto) {
        this.validate(dto);
        final MarkedTime markedTime = this.convertTo(dto);

        validateInterval(
                markedTime,
                this.markedTimeRepository.listAll()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Time.class::cast)
                        .collect(Collectors.toList()),
                Optional.of(markedTime)
        );

        return Optional.ofNullable(this.markedTimeRepository.findById(markedTime.getId()))
                .map(it -> this.markedTimeRepository.update(markedTime))
                .map(it -> new MarkedTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException(UNABLE_TO_UPDATE));
    }

    @Override
    public void delete(String id) {
        long idFormatted = Optional.ofNullable(id)
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .orElseThrow(() -> new SystemException(ID_REQUIRED));

        this.markedTimeRepository.delete(idFormatted);
    }

    @Override
    public void deleteAll() {
        this.markedTimeRepository.deleteAll();
    }

    @Override
    public MarkedTimeDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new SystemException(ID_REQUIRED);
        }

        return this.markedTimeRepository.findById(id)
                .map(it -> new MarkedTimeDTO(it.getId(), it.getInput().toString(), it.getOutput().toString()))
                .orElseThrow(() -> new SystemException(MARKED_TIME_NOT_FOUND));
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
            throw new SystemException(MARKED_TIME_NOT_NULL);
        }
    }

    private MarkedTime convertTo(MarkedTimeDTO dto) {
        return Optional.ofNullable(dto)
                .map(MarkedTime::new)
                .orElseThrow(RuntimeException::new);
    }
}
