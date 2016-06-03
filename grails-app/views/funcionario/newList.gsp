
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionário')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<br><br>
<div class="body">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <div class="buttons">
                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            </div>
            <br><br>
            <div class="list">
                <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                    <thead>
                    <th>Matrícula</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    %{-- <th>Ações</th>--}%
                    </thead>
                    <tbody>
                    <g:each in="${funcionarioInstanceList}" status="i" var="funcionario">
                        <tr>
                            <td>${funcionario.matricula}</td>
                            <td>${funcionario.nome}</td>
                            <td>${funcionario.cpf}</td>
                            %{--<td></td>--}%
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
</body>
</html>
