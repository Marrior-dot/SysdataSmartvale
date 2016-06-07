<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="bootstrap-layout"/>
    <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionario')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
    <br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.create.label" args="[entityName]"/> - [${action}]</h4>
        </div>

        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${funcionarioInstance}">
                <div class="alert alert-danger" role="alert">
                    <b>Erro ao salvar Funcionário</b>
                    <g:renderErrors bean="${funcionarioInstance}" as="list" />
                </div>
            </g:hasErrors>

            <g:if test="${flash.errors}">
                <div class="alert alert-danger" role="alert">
                    <ul>
                        <g:each in="${flash.errors}" var="err">
                            <li>${err}</li>
                        </g:each>
                    </ul>
                </div>
            </g:if>


            <div class="buttons">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'funcionario', action: 'create', params = "${[unidade_id: unidadeInstance?.id]}")}">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]"/>
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'unidade', action: 'show', id: '${unidadeInstance?.id}')}">
                    <span class="glyphicon glyphicon-list"></span>
                    RH
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'funcionario', action: 'newList')}">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]"/>
                </a>
            </div>
            <br><br>

            <g:if test="${action == Util.ACTION_VIEW}">
                <gui:tabView>
                    <gui:tab label="${entityName}" active="true">
                        <g:render template="basico" model="${[funcionarioInstance: funcionarioInstance]}"/>
                    </gui:tab>
                    <gui:tab label="Cartões">
                        <g:render template="cartao"/>
                    </gui:tab>
                </gui:tabView>
            </g:if>
            <g:else>
                <g:render template="basico"/>
            </g:else>
        </div>
    </div>
</body>
</html>
