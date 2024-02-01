package com.insight.controledejornada.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    private final int status;

    public SystemException(String message) {
        super(message);
        this.status = HttpServletResponse.SC_BAD_REQUEST;
    }
}
