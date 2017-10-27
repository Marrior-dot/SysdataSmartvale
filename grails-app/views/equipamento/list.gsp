<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'equipamento.label', default:'')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <br><br>
        <div class="body">
            <g:render template="/alertMessages"/>
            <g:render template="search" model="[controller:'equipamento']"/>
        </div>
    </body>
</html>
