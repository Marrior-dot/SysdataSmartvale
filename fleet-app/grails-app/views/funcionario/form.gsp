<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Funcionario; com.sysdata.gestaofrota.Util" %>

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

            <a class="btn btn-default"
               href="${g.createLink(controller: 'unidade', action: 'show', id: "${funcionarioInstance?.unidade?.id}")}">
                <span class="glyphicon glyphicon-triangle-left"></span>
                Unidade
            </a>

            <g:if test="${action == Util.ACTION_VIEW}">
                <a class="btn btn-default"
                   href="${g.createLink(controller: 'funcionario', action: 'create', params:[unidade_id: funcionarioInstance?.unidade?.id])}">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]"/>
                </a>
            </g:if>


            <div class="well well-lg panel-top">
                <div class="row">
                    <div class="col-md-3">
                        <h4><strong>Empresa</strong></h4>
                        <h5><g:link controller="rh" action="show" id="${funcionarioInstance?.unidade?.rh?.id}">${funcionarioInstance?.unidade?.rh?.nome}</g:link></h5>
                    </div>
                    <div class="col-md-3">
                        <h4><strong>Unidade</strong></h4>
                        <h5><g:link controller="unidade" action="show" id="${funcionarioInstance?.unidade?.id}">${funcionarioInstance?.unidade?.nome}</g:link></h5>
                    </div>
                </div>
            </div>

            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#funcionarios" data-toggle="tab">Funcionários</a></li>
                        <g:if test="${funcionarioInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO}">
                            <li><a href="#cartoes" data-toggle="tab">Cartões</a></li>
                            <g:if test="${funcionarioInstance.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO}">
                                <li><a href="#faturas" data-toggle="tab">Faturas</a></li>
                            </g:if>
                        </g:if>

                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="funcionarios">
                            <g:render template="basico" model="${[funcionarioInstance: funcionarioInstance, tamMaxEmbossing: tamMaxEmbossing]}"/>
                        </div>
                        <g:if test="${funcionarioInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO}">
                            <div class="tab-pane" id="cartoes">
                                <g:render template="/cartao/cartoes" model="[portador: funcionarioInstance.portador]"/>
                            </div>
                            <g:if test="${funcionarioInstance.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO}">
                                <div class="tab-pane" id="faturas">
                                    <g:render template="/portadorCorte/faturas" model="[portador: funcionarioInstance.portador]"/>
                                </div>
                            </g:if>
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
