
<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'veiculo.label', default: 'Veículo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="nav">
            </div>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>

                <g:render template="search" model="[controller:'veiculo']"/>
            </div>
        </div>
    </div>
    </body>
</html>
