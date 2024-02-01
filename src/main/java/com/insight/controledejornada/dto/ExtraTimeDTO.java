package com.insight.controledejornada.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExtraTimeDTO {

    private final Long id;

    private final String start;

    private final String end;
}
