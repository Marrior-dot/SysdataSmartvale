<%@ page import="com.sysdata.gestaofrota.Estabelecimento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'estabelecimento.label', default: 'Estabelecimento')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
<br><br>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.create.label" args="[entityName]"/> - [${action}]</h4>
    </div>

    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/></a>
            <g:link class="btn btn-default" controller="postoCombustivel" action="show" id="${empresaInstance?.id}">
                <span class="glyphicon glyphicon-eye-open"></span>
                <g:message code="empresa.label" default="Visualizar Empresa"/></g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <g:link class="btn btn-default" action="create" params="[empId: empresaInstance?.id]">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]"/>
                </g:link>
            </sec:ifAnyGranted>
        </div>
        <br>

        <div class="body">
            <g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${estabelecimentoInstance}">
                <div class="alert alert-danger" role="alert">
                    <p><strong>Erro ao salvar Estabelecimento</strong></p>
                    <g:renderErrors bean="${estabelecimentoInstance}" as="list"/>
                </div>
            </g:hasErrors>

            <g:render template="basico" model="[empresaInstance: empresaInstance]"/>
        </div>
    </div>
</div>

</body>
</html>
