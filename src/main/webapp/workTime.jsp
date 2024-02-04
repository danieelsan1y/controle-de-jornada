<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="com.insight.controledejornada.dto.WorkTimeDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Optional" %>
<%
    @SuppressWarnings("unchecked")
    final ArrayList<WorkTimeDTO> workTimesDTO = (ArrayList<WorkTimeDTO>) request.getAttribute("workTimesDTO");
    WorkTimeDTO workTimeDTO = null;
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
    <script src="workTime.js"></script>
    <link rel="stylesheet" href="workTime.css">
    <link rel="stylesheet" href="general.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
<div id="navbar-container"></div>
<div class="container">
    <div id="init">
        <h2>Horários de trabalho</h2>
        <div class="d-flex">
            <a id="openModalBtn" class="btn btn-success" style="margin-right: 5px"> + </a>
            <a href="workTime?type=deleteAll" class="button-red"> Remover todos </a>
        </div>
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
                            for (int i = 0; i < workTimesDTO.size(); i++) {
                        %>
                        <tr>
                            <td style="vertical-align: middle;"><%=workTimesDTO.get(i).getId()%>
                            </td>
                            <td style="vertical-align: middle;"><%=workTimesDTO.get(i).getInput()%>
                            </td>
                            <td style="vertical-align: middle;"><%=workTimesDTO.get(i).getOutput()%>
                            </td>
                            <td style="vertical-align: middle;">
                                <%
                                    workTimeDTO = workTimesDTO.get(i);
                                %>
                                <a class="btn btn-danger"
                                   href="workTime?type=delete&id=<%= workTimesDTO.get(i).getId() %>">
                                    <i class="bi bi-dash-circle"></i>
                                </a>
                                <a id="openModalUpdateBtn" class="btn btn-primary" style="margin-right: 5px">
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
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Cadastrar</h5>
                </div>
                <div class="modal-body">
                    <form id="myForm">
                        <div class="form-group">
                            <label for="input">Entrada:</label>
                            <input type="text" class="form-control" id="input" name="input" required>
                        </div>
                        <div class="form-group">
                            <label for="output">Saída:</label>
                            <input type="text" class="form-control" id="output" name="output" required>
                        </div>
                        <div class="d-flex" style="margin-top: 10px">
                            <button type="submit" class="btn btn-primary" id="enviarBtn" style="margin-right: 5px">
                                Enviar
                            </button>
                            <a href="workTime?type=list" class="button-red" id="cancelarLink">Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modalUpdate" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalUpdateLabel">Atualizar</h5>
            </div>
            <div class="modal-body">
                <form id="updateForm">
                    <div class="form-group">
                        <label for="input">Número:</label>
                        <input type="text" class="form-control" id="updateId" name="id" required readonly
                               value="<%=Optional.ofNullable(workTimeDTO).map(WorkTimeDTO::getId).map(String::valueOf).orElse("")%>"
                        >
                    </div>
                    <div class="form-group">
                        <label for="input">Entrada:</label>
                        <input type="text" class="form-control" id="updateInput" name="input" required
                               value="<%=Optional.ofNullable(workTimeDTO).map(WorkTimeDTO::getInput).orElse("")%>"
                        >
                    </div>
                    <div class="form-group">
                        <label for="input">Saída:</label>
                        <input type="text" class="form-control" id="UpdateOutput" name="output" required
                               value="<%=Optional.ofNullable(workTimeDTO).map(WorkTimeDTO::getOutput).orElse("")%>"
                        >
                    </div>
                    <div class="d-flex" style="margin-top: 10px">
                        <button type="submit" class="btn btn-primary" id="updateButton" style="margin-right: 5px">
                            Atualizar
                        </button>
                        <a href="workTime?type=list" class="button-red" id="cancelarUpdateButton">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>