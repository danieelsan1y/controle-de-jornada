package com.insight.controledejornada.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Message {

    private final String message;

    private final int status;

}
