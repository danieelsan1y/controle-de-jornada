package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.exception.SystemException;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException {

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("markedTime.jsp");

        request.setAttribute("markedTimesDTO", this.markedTimeService.listAll());
        requestDispatcher.forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        final String input = Optional.ofNullable(request.getParameter("input"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a entrada."));

        final String output = Optional.ofNullable(request.getParameter("output"))
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new SystemException("É necessário informar a saida."));

        markedTimeService.insert(new MarkedTimeDTO(null, input, output));
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws SystemException {

    }

    public void destroy() {
    }
}
