<%@ page import="com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Funcionario; com.sysdata.gestaofrota.Util" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
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
                <div class="alert alert-info">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger">${flash.error}</div>
            </g:if>

            <g:hasErrors bean="${funcionarioInstance}">
                <div class="alert alert-danger">
                    <b>Erro ao salvar Funcionário</b>
                    <g:renderErrors bean="${funcionarioInstance}" as="list" />
                </div>
            </g:hasErrors>

            <div class="buttons">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'funcionario', action: 'create', params:[unidade_id: unidadeInstance?.id])}">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]"/>
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'unidade', action: 'show', id: "${unidadeInstance.id}")}">
                    <span class="glyphicon glyphicon-list"></span>
                    Centro de Custo
                </a>

                <a class="btn btn-default"
                   href="${g.createLink(controller: 'funcionario', action: 'list', params:[unidade_id: unidadeInstance?.id])}">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]"/>
                </a>
            </div>
            <br>

            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#funcionarios" data-toggle="tab">Funcionários</a></li>
                        <g:if test="${funcionarioInstance.unidade.rh.vinculoCartao==TipoVinculoCartao.FUNCIONARIO}">
                            <li><a href="#cartoes" data-toggle="tab">Cartões</a></li>
                        </g:if>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="funcionarios">
                            <g:render template="basico" model="${[funcionarioInstance: funcionarioInstance, unidadeInstance: unidadeInstance, tamMaxEmbossing: tamMaxEmbossing]}"/>
                        </div>
                        <g:if test="${funcionarioInstance.unidade.rh.vinculoCartao==TipoVinculoCartao.FUNCIONARIO}">
                            <div class="tab-pane" id="cartoes">
                                <g:render template="cartao" model="[portador: funcionarioInstance.portador]"/>
                            </div>
                        </g:if>
                    </div>
                </div>
            </g:if>
            <g:else>
                <g:render template="basico" model="[funcionario: funcionario]"/>
            </g:else>
        </div>
    </div>
</body>
</html>
