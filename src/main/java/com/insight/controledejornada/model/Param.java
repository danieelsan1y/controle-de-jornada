package com.insight.controledejornada.model;

import com.insight.controledejornada.exception.SystemException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Param {

    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    DELETE_ALL("deleteAll"),
    LIST("list");

    private final String name;


    public static Param valueOfBy(String name) {
        return Arrays.stream(Param.values())
                .filter(it -> it.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new SystemException(String.format("param '%s' não foi mapeado",name)));
    }

}
