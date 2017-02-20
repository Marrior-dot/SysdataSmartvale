<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'marcaVeiculo.label', default: 'Marca de Veículo')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.show.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="nav">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="list">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]" /></g:link>
                <g:link class="btn btn-default" action="create">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" /></g:link>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>
                <ul class="properties">
                    <li>
                        <span class="p-label"><g:message code="marcaVeiculo.id.label" default="Id" /></span>
                        <span class="p-value">${fieldValue(bean: marcaVeiculoInstance, field: "id")}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="marcaVeiculo.nome.label" default="Nome" /></span>
                        <span class="p-value">${fieldValue(bean: marcaVeiculoInstance, field: "nome")}</span>
                    </li>
                </ul>
                <div class="buttons">
                    <g:form>
                        <g:hiddenField name="id" value="${marcaVeiculoInstance?.id}" />
                        <span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                        <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:form>
                </div>
            </div>
        </div>
    </div>

    </body>
</html>
