
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionário')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="body">

            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>

            <br><br>
            
            <g:render template="search" model="[controller:'funcionario', unidade_id: unidadeInstance?.id]"/>

</div>
</body>
</html>
