<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <title>Processamentos</title>
</head>
<body>

<div class="panel panel-default panel-top">

    <div class="panel-heading">
        <h4>Processamentos</h4>
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
            <g:link class="btn btn-default" action="executeAll"><span class="glyphicon glyphicon-play-circle"></span> Executar Todos</g:link>
        </p>


        <g:if test="${processingList}">
            <table class="table table-bordered table-striped">
                <thead>
                <th>Ordem</th>
                <th>Processamento</th>
                <th>Serviço</th>
                <th>Opções</th>
                </thead>
                <tbody>
                <g:each in="${processingList}" var="p">
                    <tr>
                        <td>${p.order}</td>
                        <td>${p.name}</td>
                        <td>${p.service}</td>
                        <td class="text-center">
                            <g:link class="btn btn-sm btn-primary" title="Executar" controller="processamento" action="execute" id="${p.id}">
                                <i class="glyphicon glyphicon-play"></i></g:link>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <g:paginate total="${processingCount}" params="${params}"></g:paginate>
        </g:if>
        <g:else>
            <div class="well text-center">Não há Processamentos</div>
        </g:else>
    </div>
</div>
</body>
</html>