package com.insight.controledejornada.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkTimeDTO {

    private final Long id;

    private final String input;

    private final String output;

}
