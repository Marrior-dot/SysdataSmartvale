
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="layout-restrito" />
    <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionário')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="body">
    <br><br>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]"/></h4>
            </div>

            <div class="panel-body">
                <g:if test="${flash.message}">
                    <div class="alert alert-info" role="alert">${flash.message}</div>
                </g:if>
                <g:form>
                    <br><br>

                    <div class="list">
                        <g:render template="search" model="[controller:'funcionario', unidade_id: unidadeInstance?.id]"/>
                    </div>
                </g:form>
            </div>
        </div>
</div>
</body>
</html>
