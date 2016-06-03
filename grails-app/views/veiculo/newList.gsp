
<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'veiculo.label', default: 'Veículo')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>

<div class="body">
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            <br><br>
            <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                <thead>
                <th>Placa</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Ano</th>
                </thead>
                <tbody>
                <g:each in="${veiculoInstanceList}" status="i" var="veiculo">
                    <tr>
                        <td>${veiculo.placa}</td>
                        <td>${veiculo.marca.nome}</td>
                        <td>${veiculo.modelo}</td>
                        <td>${veiculo.anoFabricacao}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
