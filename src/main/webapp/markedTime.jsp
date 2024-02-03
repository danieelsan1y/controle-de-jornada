<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.insight.controledejornada.dto.MarkedTimeDTO" %>
<%
    @SuppressWarnings("unchecked")
    final ArrayList<MarkedTimeDTO> markedTimesDTO = (ArrayList<MarkedTimeDTO>) request.getAttribute("markedTimesDTO");
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
        <h2>Marcações Feitas</h2>
        <a href="form.html" class="buton-blue"> + </a>
        <a href="markedTime?type=deleteAll" class="button-red"> Remover todos </a>
    </div>
    <div class="container">
        <div class="container">
            <div class="row">
                <div class="cold-md-7">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Entrada</th>
                            <th>Saída</th>
                            <th>Ação</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (int i = 0; i < markedTimesDTO.size(); i++) {
                        %>
                        <tr>
                            <td><%=markedTimesDTO.get(i).getId()%>
                            </td>
                            <td><%=markedTimesDTO.get(i).getInput()%>
                            </td>
                            <td><%=markedTimesDTO.get(i).getOutput()%>
                            </td>
                            <td>
                                <a href="markedTime?type=delete&id=<%= markedTimesDTO.get(i).getId() %>">
                                    <i class="bi bi-dash-circle"></i>
                                </a>
                                <a href="form.html">
                                    <i class="bi bi-arrow-clockwise"></i>
                                </a>

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