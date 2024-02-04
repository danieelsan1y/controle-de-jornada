package com.insight.controledejornada.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MarkedTimeDTO {

    public static String name = "markedTimeDTO";

    public static String listName = "markedTimesDTO";

    private final Long id;

    private final String input;

    private final String output;
}
