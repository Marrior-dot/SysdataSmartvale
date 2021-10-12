<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <title>Histórico de Processamentos</title>
</head>
<body>

<div class="panel panel-default panel-top">

    <div class="panel-heading">
        <h4>Histórico de Processamentos</h4>
    </div>

    <div class="panel-body">

        <g:if test="${flash.message}">
            <div class="alert alert-info" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger" role="status">${flash.error}</div>
        </g:if>

        <p>
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
        </p>

        <g:if test="${historicoList}">
            <table class="table table-bordered table-striped">
                <thead>
                <th>Data/Hora Início</th>
                <th>Data/Hora Fim</th>
                <th>Processamento</th>
                <th>Status</th>
                </thead>
                <tbody>
                <g:each in="${historicoList}" var="h">
                    <tr>
                        <td><g:link action="show" id="${h.id}"><g:formatDate date="${h.startTime}" format="dd/MM/yy HH:mm:ss"></g:formatDate></g:link></td>
                        <td><g:formatDate date="${h.endTime}" format="dd/MM/yy HH:mm:ss"></g:formatDate></td>
                        <td>${h.processing.name}</td>
                        <td>${h.executionStatus.name}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <g:paginate total="${historicoCount}"></g:paginate>
        </g:if>
        <g:else>
            <div class="well text-center">Não há Histórico de Processamentos</div>
        </g:else>
    </div>
</div>
</body>
</html>