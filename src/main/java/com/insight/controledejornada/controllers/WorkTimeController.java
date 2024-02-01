package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.service.WorkTimeService;
import com.insight.controledejornada.service.impl.WorkTimeServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@WebServlet(name = "workTime", value = "/workTime")
@RequiredArgsConstructor
public class WorkTimeController extends HttpServlet {

    private final WorkTimeService workTimeService;

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        final String param = Optional.ofNullable(request.getParameter("type"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar o tipo."));

        if (param.equals("list")) {
            request.setAttribute("workTimes", this.workTimeService.listAll());
        } else if (param.equals("find")) {
            final String id = Optional.ofNullable(request.getParameter("id"))
                    .filter(StringUtils::isNoneBlank)
                    .orElseThrow(() -> new SystemException("id é necessário."));
            request.setAttribute("workTIme", this.workTimeService.findById(Long.parseLong(id)));
        } else {
            throw new SystemException("tipo não válido");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        final String input = Optional.ofNullable(request.getParameter("input"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a entrada."));

        final String output = Optional.ofNullable(request.getParameter("output"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a saida."));

        workTimeService.insert(new WorkTimeDTO(null, input, output));
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws SystemException {

    }

    public void destroy() {
    }
}
