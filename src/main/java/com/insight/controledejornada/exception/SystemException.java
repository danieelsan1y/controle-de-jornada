package com.insight.controledejornada.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import java.util.Optional;

@Getter
public class SystemException extends RuntimeException {

    private final int status;

    public SystemException(Message message) {
        super(message.getType().concat(message.getDescription()));
        this.status = HttpServletResponse.SC_BAD_REQUEST;
    }

    public SystemException(String message) {
        super(message);
        this.status = HttpServletResponse.SC_BAD_REQUEST;
    }
}
