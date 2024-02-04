package com.insight.controledejornada.controllers;

import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.repositories.impl.MakedTimeRepositoryImpl;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.DelayHourService;
import com.insight.controledejornada.service.impl.DelayHourServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "delayhour", value = "/delayHour")
public class DelayHourController extends HttpServlet {

    static int teste = 0;

    private final DelayHourService delayHourService = new DelayHourServiceImpl(new WorkTimeRepositoryImpl(), new MakedTimeRepositoryImpl());

    public void init() {
    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SystemException, ServletException, IOException {
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("delayHour.jsp");

        request.setAttribute("delayHourDTO", this.delayHourService.getDelayHours());
        requestDispatcher.forward(request, response);
    }
}
