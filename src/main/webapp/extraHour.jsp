<%@ page contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.insight.controledejornada.dto.ExtraHourDTO" %>
<%
    @SuppressWarnings("unchecked")
    final ArrayList<ExtraHourDTO> extraHourDTO = (ArrayList<ExtraHourDTO>) request.getAttribute("extraHoursDTO");
%>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title>Controle de Jornada</title>
    <script src="script.js"></script>
    <link rel="stylesheet" href="workTime.css">
    <link rel="stylesheet" href="general.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
<div id="navbar-container"></div>
<div class="container">
    <div id="init">
        <h2>Horas extras</h2>
    </div>
    <div class="container">
        <div class="container">
            <div class="row">
                <div class="cold-md-7">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Início</th>
                            <th>Fim</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (int i = 0; i < extraHourDTO.size(); i++) {
                        %>
                        <tr>
                            <td><%=i + 1%>
                            </td>
                            <td><%=extraHourDTO.get(i).getStart()%>
                            </td>
                            <td><%=extraHourDTO.get(i).getEnd()%>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>