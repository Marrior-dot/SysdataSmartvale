
<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'marcaVeiculo.label', default: 'Marca de Veículo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">
                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                <g:link action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" />
                </g:link>
                <br><br>
                <div class="body">
                    <g:if test="${flash.message}">
                        <div class="message">${flash.message}</div>
                    </g:if>
                    <div class="list">
                        <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                            <thead>
                            <tr>
                                <g:sortableColumn property="id" title="${message(code: 'marcaVeiculo.id.label', default: 'Id')}" />
                                <g:sortableColumn property="nome" title="${message(code: 'marcaVeiculo.nome.label', default: 'Nome')}" />
                                <g:sortableColumn property="abreviacao" title="${message(code: 'marcaVeiculo.abreviacao.label', default: 'Abreviação')}" />
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${marcaVeiculoInstanceList}" status="i" var="marcaVeiculoInstance">
                                <tr>
                                    <td><g:link action="show" id="${marcaVeiculoInstance.id}">${fieldValue(bean: marcaVeiculoInstance, field: "id")}</g:link></td>
                                    <td>${fieldValue(bean: marcaVeiculoInstance, field: "nome")}</td>
                                    <td>${fieldValue(bean: marcaVeiculoInstance, field: "abreviacao")}</td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                    <div class="paginateButtons">
                        <g:paginate total="${marcaVeiculoInstanceTotal}" />
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
