package com.insight.controledejornada.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Message {
    INPUT_REQUIRED("clientError: ", "É necessário informar a entrada."),
    OUTPUT_REQUIRED("clientError: ", "É necessário informar a saída."),
    ID_REQUIRED("clientError: ", "É necessário informar o id."),
    WRONG_HOUR_AND_MINUTE("clientError: ", "Formato de hora e minuto incorreto."),
    THREE_WORK_TIME("clientError: ", "É Possível cadastrar apenas 3 Horários de trabalho."),
    INCORRECT_HOUT_AND_MINUTE_FORMAT("clientError: ", "Formato de hora e minuto incorreto."),
    TIME_CONFLICT("clientError: ", "Há um conflito nos horários cadastrados, pois está dentro do intervalo de outro cadastro."),
    INPUT_AND_OUTPUT_EQUALS("clientError: ", "Entrada e saida não podem ser iguais."),
    TIME_OVERLAY("clientError: ", "Entrada e saida não pode sobrepor outras horas."),
    UNABLE_TO_UPDATE("error: ", "Não foi possível atualizar."),
    MARKED_TIME_NOT_FOUND("error: ", "o Objeto 'markedTime' não foi encontrado."),
    MARKED_TIME_NOT_NULL("error: ", "O Objeto 'markedTime' não pode ser nulo."),
    WORKTIME_TIME_NOT_FOUND("error: ", "o Objeto 'workTime' não foi encontrado."),
    WORKTIME_TIME_NOT_NULL("error: ", "O Objeto 'workTime' não pode ser nulo."),
    TYPE_REQUIRED("error: ", "É necessário informar o type.");

    private final String type;

    private final String description;
}
