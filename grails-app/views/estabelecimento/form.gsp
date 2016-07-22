<%@ page import="com.sysdata.gestaofrota.Estabelecimento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'estabelecimento.label', default: 'Estabelecimento')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
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
                    <g:link class="btn btn-default" action="create" params="[empId:empresaInstance?.id]">
                        <span class="glyphicon glyphicon-plus"></span>
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link>
                </sec:ifAnyGranted>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${estabelecimentoInstance}">
                    <div class="errors">
                        <span style="font-weight:bold;padding-left:10px">Erro ao salvar Estabelecimento</span>
                        <g:renderErrors bean="${estabelecimentoInstance}" as="list" />
                    </div>
                </g:hasErrors>
                <g:form method="post" >
                    <g:hiddenField name="id" value="${estabelecimentoInstance?.id}" />
                    <g:hiddenField name="version" value="${estabelecimentoInstance?.version}" />
                    <g:hiddenField name="empId" value="${empresaInstance?.id}" />
                    <g:hiddenField name="action" value="${action}"/>

                    <g:render template="basico"/>

                    <div class="buttons">
                        <g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
                            <span class="button"><g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                        </g:if>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                            <g:if test="${action==Util.ACTION_VIEW}">
                                <span class="button"><g:actionSubmit class="btn btn-default"action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                                <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                            </g:if>
                        </sec:ifAnyGranted>
                    </div>

                </g:form>
            </div>
        </div>
    </div>

    </body>
</html>
