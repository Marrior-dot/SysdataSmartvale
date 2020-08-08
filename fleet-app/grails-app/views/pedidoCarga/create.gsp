<%@ page import="com.sysdata.gestaofrota.Util" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

        <meta name="layout" content="layout-restrito"/>
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}"/>
        <title><g:message code="default.new.label" args="[entityName]"/></title>
    </head>

    <body>
        <br/>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.create.label" args="[entityName]"/> - [${Util.ACTION_NEW}]</h4>
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


            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>
            <g:link class="btn btn-default" action="list">
                <i class="glyphicon glyphicon-th-list"></i>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>

            <g:form name="defaultForm">
                <g:render template="form" model="${[action: Util.ACTION_NEW]}"/>
                <button id="submitButton" type="submit" name="_action_save" class="btn btn-success">
                    <span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.create.label" default="Create"></g:message>
                </button>

            </g:form>
        </div>
    </div>


    <asset:javascript src="pedidoCarga.js"></asset:javascript>


    </body>
</html>