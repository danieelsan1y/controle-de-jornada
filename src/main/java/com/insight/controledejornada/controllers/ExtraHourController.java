package com.insight.controledejornada.controllers;

import com.insight.controledejornada.dto.MarkedTimeDTO;
import com.insight.controledejornada.dto.WorkTimeDTO;
import com.insight.controledejornada.exception.SystemException;
import com.insight.controledejornada.repositories.impl.MakedTimeRepositoryImpl;
import com.insight.controledejornada.repositories.impl.WorkTimeRepositoryImpl;
import com.insight.controledejornada.service.DelayHourService;
import com.insight.controledejornada.service.ExtraHourService;
import com.insight.controledejornada.service.MarkedTimeService;
import com.insight.controledejornada.service.WorkTimeService;
import com.insight.controledejornada.service.impl.DelayHourServiceImpl;
import com.insight.controledejornada.service.impl.ExtraHourServiceImpl;
import com.insight.controledejornada.service.impl.MarkedTimeServiceImpl;
import com.insight.controledejornada.service.impl.WorkTimeServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.insight.controledejornada.controllers.DelayHourController.teste;

@WebServlet(name = "extraHour", value = "/extraHour")
public class ExtraHourController extends HttpServlet {

    private final ExtraHourService extraHourService = new ExtraHourServiceImpl(new WorkTimeRepositoryImpl(), new MakedTimeRepositoryImpl());

    public void init() {
    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SystemException, ServletException, IOException {
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("extraHour.jsp");

        request.setAttribute("extraHoursDTO", this.extraHourService.getExtraHours());
        requestDispatcher.forward(request, response);
    }
}
