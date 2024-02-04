package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.ParamContent;
import com.insight.controledejornada.model.WorkTime;
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

import static com.insight.controledejornada.exception.Message.*;
import static com.insight.controledejornada.model.Param.*;
import static com.insight.controledejornada.model.WorkTime.pageName;

@WebServlet(name = "workTime", value = "/workTime")
public class WorkTimeController extends HttpServlet {

    private final WorkTimeService workTimeService = new WorkTimeServiceImpl(new WorkTimeRepositoryImpl());

    public void init() {
    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SystemException, ServletException, IOException {
        String content = ParamContent.LIST.getName();

        if (StringUtils.isNotBlank(request.getParameter(TYPE.getName()))) {
            content = request.getParameter(TYPE.getName());
        }

        final String id = request.getParameter(ID.getName());

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(WorkTime.pageName);

        this.processRequestByParam(request, content, id);

        this.setAttribute(request);

        requestDispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException {
        final String input = Optional.ofNullable(request.getParameter(INPUT.getName()))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException(INPUT_REQUIRED));

        final String output = Optional.ofNullable(request.getParameter(OUTPUT.getName()))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException(OUTPUT_REQUIRED));

        workTimeService.insert(new WorkTimeDTO(null, input, output));

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(pageName);

        this.setAttribute(request);

        requestDispatcher.forward(request, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        final String input = Optional.ofNullable(request.getParameter(INPUT.getName()))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException(INPUT_REQUIRED));

        final String output = Optional.ofNullable(request.getParameter(OUTPUT.getName()))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException(OUTPUT_REQUIRED));

        final String id = Optional.ofNullable(request.getParameter(ID.getName()))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException(ID_REQUIRED));

        this.workTimeService.update(new WorkTimeDTO(Long.parseLong(id), input, output));
    }

    private void processRequestByParam(HttpServletRequest request, String content, String id) {
        switch (ParamContent.valueOfBy(content)) {
            case LIST:
                this.list(request);
                break;
            case DELETE:
                this.delete(request, id);
                break;
            case DELETE_ALL:
                this.deleteAll(request);
                break;
            case FIND_BY_ID:
                this.findById(request, id);
                break;
            default:
                throw new IllegalArgumentException(String.format("ParamContent '%s' não foi mapeado", content));
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
            throw new SystemException(ID_REQUIRED);
        }

        this.workTimeService.delete(id);
        this.setAttribute(request);
    }

    private void findById(HttpServletRequest request, String id) {
        if (StringUtils.isBlank(id)) {
            throw new SystemException(ID_REQUIRED);
        }

        final WorkTimeDTO workTimeDTO = this.workTimeService.findById(Long.parseLong(id));
        request.setAttribute(WorkTimeDTO.name, workTimeDTO);
    }

    private void setAttribute(HttpServletRequest request) {
        request.setAttribute(WorkTimeDTO.listName, this.workTimeService.listAll());
    }
}
