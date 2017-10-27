
<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'veiculo.label', default: 'Veículo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

    <br><br><br>
            <div class="nav">
            </div>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>

                <g:render template="search" model="[controller:'veiculo']"/>
            </div>

    </body>
</html>
