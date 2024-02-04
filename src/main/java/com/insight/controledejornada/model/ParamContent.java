package com.insight.controledejornada.model;

import com.insight.controledejornada.exception.SystemException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ParamContent {

    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    DELETE_ALL("deleteAll"),
    LIST("list"),
    FIND_BY_ID("findById");

    private final String name;

    public static ParamContent valueOfBy(String name) {
        return Arrays.stream(ParamContent.values())
                .filter(it -> it.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new SystemException(String.format("param '%s' não foi mapeado", name)));
    }
}
