
<%@ page import="com.sysdata.gestaofrota.Cidade" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'cidade.label', default: 'Cidade')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default">

            <div class="panel-heading">
                Cidade
            </div>

            <div class="panel-body">

                <table class="table table-stripped">
                    <th>#</th>
                    <td>${cidadeInstance.id}</td>
                    <th>Nome</th>
                    <td>${cidadeInstance.nome}</td>
                    <th>UF</th>
                    <td>${cidadeInstance.estado.uf}</td>
                </table>
            </div>
        </div>
    </body>
</html>
