package com.insight.controledejornada.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Param {

    INPUT("input"),

    OUTPUT("output"),

    ID("id"),

    TYPE("type");

    private final String name;
}
