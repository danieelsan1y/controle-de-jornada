package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.model.Param;
import com.insight.controledejornada.repositories.impl.MakedTimeRepositoryImpl;
import com.insight.controledejornada.service.ExtraHourService;
import com.insight.controledejornada.service.MarkedTimeService;
import com.insight.controledejornada.service.impl.MarkedTimeServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "markedTime", value = "/markedTime")
public class MarkedTimeController extends HttpServlet {

    private final MarkedTimeService markedTimeService = new MarkedTimeServiceImpl(new MakedTimeRepositoryImpl());

    public void init() {

    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SystemException, ServletException, IOException {
        String param = "list";

        if (StringUtils.isNotBlank(request.getParameter("type"))) {
            param = request.getParameter("type");
        }

        final String id = request.getParameter("id");

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("markedTime.jsp");

        this.processRequestByParam(request, param, id);

        this.setAttribute(request);

        requestDispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException {
        final String input = Optional.ofNullable(request.getParameter("input"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a entrada."));

        final String output = Optional.ofNullable(request.getParameter("output"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a saida."));

        markedTimeService.insert(new MarkedTimeDTO(null, input, output));

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("markedTime.jsp");

        this.setAttribute(request);

        requestDispatcher.forward(request, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        final String input = Optional.ofNullable(request.getParameter("input"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("o param 'entrada' é necessário."));

        final String output = Optional.ofNullable(request.getParameter("output"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("o param 'saída' é necessário."));

        final String id = Optional.ofNullable(request.getParameter("id"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("o param 'id' é necessário"));

        this.markedTimeService.update(new MarkedTimeDTO(Long.parseLong(id), input, output));
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
            case FIND_BY_ID:
                this.findById(request, id);
                break;
            default:
                throw new SystemException(String.format("param '%s' não foi mapeado", param));
        }
    }

    private void list(HttpServletRequest request) {
        this.setAttribute(request);
    }

    private void deleteAll(HttpServletRequest request) {
        this.markedTimeService.deleteAll();
        this.setAttribute(request);
    }

    private void delete(HttpServletRequest request, String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("o param 'id' é necessário");
        }

        this.markedTimeService.delete(id);
        this.setAttribute(request);
    }

    private void findById(HttpServletRequest request, String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("o param 'id' é necessário");
        }

        final MarkedTimeDTO markedTimeDTO = this.markedTimeService.findById(Long.parseLong(id));
        request.setAttribute("markedTimeDTO", markedTimeDTO);
    }

    private void setAttribute(HttpServletRequest request) {
        request.setAttribute("markedTimesDTO", this.markedTimeService.listAll());
    }
}
