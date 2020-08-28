
<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.Rh" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="layout-restrito" />
    <g:set var="entityName" value="Credenciado" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <g:link action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" />
                </g:link>
            </sec:ifAnyGranted>
            <g:render template="search" model="[controller:'postoCombustivel']"/>

        </div>
    </div>
</body>
</html>