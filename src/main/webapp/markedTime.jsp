<%@ page contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static com.insight.controledejornada.dto.MarkedTimeDTO.listName" %>
<%@ page import="com.insight.controledejornada.dto.MarkedTimeDTO" %>
<%
    @SuppressWarnings("unchecked")
    final ArrayList<MarkedTimeDTO> markedTimesDTO = (ArrayList<MarkedTimeDTO>) request.getAttribute(listName);
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
    <script src="markedTime.js"></script>
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
            <a class="button-red deleteAllButton" href="#"> Remover todos </a>
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
                            for (int i = 0; i < markedTimesDTO.size(); i++) {
                        %>
                        <tr>
                            <td style="vertical-align: middle;"><%=markedTimesDTO.get(i).getId()%>
                            </td>
                            <td style="vertical-align: middle;"><%=markedTimesDTO.get(i).getInput()%>
                            </td>
                            <td style="vertical-align: middle;"><%=markedTimesDTO.get(i).getOutput()%>
                            </td>
                            <td style="vertical-align: middle;">
                                <a class="btn btn-danger deleteButton"
                                   data-rowdeleteid="<%= markedTimesDTO.get(i).getId()%>">
                                    <i class="bi bi-dash-circle"></i>
                                </a>
                                <a class="btn btn-primary openModalUpdateBtn" style="margin-right: 5px"
                                   data-rowid="<%= markedTimesDTO.get(i).getId()%>"
                                   data-rowinput="<%= markedTimesDTO.get(i).getInput()%>"
                                   data-rowoutput="<%= markedTimesDTO.get(i).getOutput()%>"
                                >
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
                            <input type="time" class="form-control" id="input" name="input" required>
                        </div>
                        <div class="form-group">
                            <label for="output">Saída:</label>
                            <input type="time" class="form-control" id="output" name="output" required>
                        </div>
                        <div class="d-flex" style="margin-top: 10px">
                            <button type="submit" class="btn btn-primary" id="enviarBtn" style="margin-right: 5px">
                                Enviar
                            </button>
                            <a href="markedTime?type=list" class="button-red" id="cancelarLink">Cancelar</a>
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
                        >
                    </div>
                    <div class="form-group">
                        <label for="input">Entrada:</label>
                        <input type="time" class="form-control" id="updateInput" name="input" required
                        >
                    </div>
                    <div class="form-group">
                        <label for="input">Saída:</label>
                        <input type="time" class="form-control" id="updateOutput" name="output" required
                        >
                    </div>
                    <div class="d-flex" style="margin-top: 10px">
                        <button type="submit" class="btn btn-primary" id="updateButton" style="margin-right: 5px">
                            Atualizar
                        </button>
                        <a href="markedTime?type=list" class="button-red" id="cancelarUpdateButton">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>