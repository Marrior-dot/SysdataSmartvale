<%--
  Created by IntelliJ IDEA.
  User: hyago
  Date: 08/07/16
  Time: 17:39
--%>

<%@ page import="com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.StatusPedidoCarga" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
<br/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.edit.label" args="[entityName]"/></h4>
    </div>

    <div class="panel-body">

        <g:if test="${flash.errors}">
            <div class="alert alert-danger">${flash.errors}</div>
        </g:if>

        <g:hasErrors bean="${pedidoCargaInstance}">
            <div class="alert alert-danger">
                <b>Erro ao salvar Pedido Carga</b>
                <g:renderErrors bean="${pedidoCargaInstance}" as="list" />
            </div>
        </g:hasErrors>

        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>

            <g:link class="btn btn-default" action="list">
                <i class="glyphicon glyphicon-th-list"></i>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>

            <g:link class="btn btn-default" action="create" params="${[unidade_id: pedidoCargaInstance?.unidade?.rh?.id]}">
                <i class="glyphicon glyphicon-plus"></i>
                <g:message code="default.new.label" args="${[entityName]}"/>
            </g:link>
        </div>

        <br/>
        <g:form controller="pedidoCarga" method="POST" action="update" name="defaultForm">
            <g:render template="form" model="${[pedidoCargaInstance: pedidoCargaInstance, action: Util.ACTION_EDIT]}"/>

            <g:actionSubmit id="submitButton" disabled="" action="update" class="btn btn-default" value="${message(code: 'default.button.update.label', default: 'Alterar')}"/>

            <g:actionSubmit resource="${pedidoCargaInstance.id}" action="delete" class="btn btn-default" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Você tem certeza?')}');"/>
        </g:form>
    </div>
</div>

</body>
</html>