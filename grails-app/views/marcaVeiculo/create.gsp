

<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'marcaVeiculo.label', default: 'Marca de Veículo')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.create.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="nav">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="list">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]" /></g:link>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${marcaVeiculoInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${marcaVeiculoInstance}" as="list" />
                    </div>
                </g:hasErrors>
                <g:form action="save" method="post" >
                    <div class="dialog">
                        <table>
                            <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: marcaVeiculoInstance, field: 'nome', 'errors')}">
                                    <label for="nome"><g:message code="marcaVeiculo.nome.label" default="Nome" /></label>
                                    <g:textField class="form-control" name="nome" value="${marcaVeiculoInstance?.nome}" />
                                </td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                    <br><br>
                    <div class="buttons">
                        <span class="button"><g:submitButton name="create" class="btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    </body>
</html>
