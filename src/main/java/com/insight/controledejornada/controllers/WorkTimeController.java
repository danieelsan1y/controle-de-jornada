package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.Param;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.WorkTimeService;
import com.insight.controledejornada.service.impl.WorkTimeServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "workTime", value = "/workTime")
public class WorkTimeController extends HttpServlet {

    private final WorkTimeService workTimeService = new WorkTimeServiceImpl(new WorkTimeRepositoryImpl());

    public void init() {
    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SystemException, ServletException, IOException {
        final String param = Optional.ofNullable(request.getParameter("type"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar o tipo."));

        final String id = request.getParameter("id");

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("workTime.jsp");

        this.processRequestByParam(request, param, id);

        requestDispatcher.forward(request, response);
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

    private void processRequestByParam(HttpServletRequest request, String param, String id) {
        switch (Param.valueOfBy(param)) {
            case LIST:
                this.list(request);
                break;
            case DELETE:
                this.delete(request, id);
                break;
            case DELETE_ALL:
                this.deleteAll(request);
                break;
            default:
                throw new SystemException(String.format("param '%s' não foi mapeado", param));
        }
    }

    private void list(HttpServletRequest request) {
        this.setAttribute(request);
    }

    private void deleteAll(HttpServletRequest request) {
        this.workTimeService.deleteAll();
        this.setAttribute(request);
    }

    private void delete(HttpServletRequest request, String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("o param 'id' é necessário");
        }

        this.workTimeService.delete(id);
        this.setAttribute(request);
    }

    private void setAttribute(HttpServletRequest request) {
        request.setAttribute("workTimesDTO", this.workTimeService.listAll());
    }
}
