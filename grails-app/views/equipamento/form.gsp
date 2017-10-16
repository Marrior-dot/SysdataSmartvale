<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="bootstrap-layout"/>
    <g:set var="entityName" value="${message(code: 'equipamento.label', default: '')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
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
                <g:message code="default.home.label"/>
            </a>
            <g:link class="btn btn-default" action="list">
                <span class="glyphicon glyphicon-list"></span>
                Lista de Equipamento
            </g:link>
            <g:link class="btn btn-default" action="create" params="[unidade_id: unidadeInstance?.id]">
                <span class="glyphicon glyphicon-plus"></span>
                Criar Equipamento
            </g:link>
        </div>
        <br><br>

        <div class="body">

            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger" role="alert">${flash.error}</div>
            </g:if>

            <g:hasErrors bean="${equipamentoInstance}">
                <div class="alert alert-danger" role="alert">
                    <g:renderErrors bean="${equipamentoInstance}" as="list"/>
                </div>
            </g:hasErrors>

            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab1" data-toggle="tab">Dados Básicos</a></li>
                        <li><a href="#tab2" data-toggle="tab">Funcionários</a></li>
                    </ul>

                    <div class="tab-content">
                        <br><br>

                        <div class="tab-pane active" id="tab1">
                            <g:render template="basico"
                                      model="${[action: action, equipamentoInstance: equipamentoInstance, unidadeInstance: unidadeInstance]}"/>
                        </div>

                        <div class="tab-pane" id="tab2">
                            <g:render template="/maquinaMotorizada/funcionarios" model="${[instance: equipamentoInstance, instanceName: 'Equipamento']}"/>
                        </div>
                    </div>
                </div>
            </g:if>
            <g:else>
                <g:render template="basico" model="${[action: action, equipamentoInstance: equipamentoInstance, unidadeInstance: unidadeInstance, tamMaxEmbossing: tamMaxEmbossing]}"/>
            </g:else>

        </div>
    </div>
</div>

</body>
</html>
