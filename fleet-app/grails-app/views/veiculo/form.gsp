<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />

        <g:set var="entityName" value="${message(code: 'veiculo.label', default: 'Veiculo')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>

            <div class="panel-body">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
                </a>
                <a class="btn btn-default"
                   href="${g.createLink(controller: 'unidade', action: 'show', id: "${veiculoInstance?.unidade?.id}")}">
                    <span class="glyphicon glyphicon-triangle-left"></span>
                    Unidade
                </a>
                <g:link class="btn btn-default" action="create" params="[unidade_id: veiculoInstance?.unidade?.id]">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" />
                    </g:link>

                <div class="well well-lg panel-top">
                    <div class="row">
                        <div class="col-md-3">
                            <h4><strong>Empresa</strong></h4>
                            <h5><g:link controller="rh" action="show" id="${veiculoInstance?.unidade?.rh?.id}">${veiculoInstance?.unidade?.rh?.nome}</g:link></h5>
                        </div>
                        <div class="col-md-3">
                            <h4><strong>Unidade</strong></h4>
                            <h5><g:link controller="unidade" action="show" id="${veiculoInstance?.unidade?.id}">${veiculoInstance?.unidade?.nome}</g:link></h5>
                        </div>
                    </div>
                </div>

                <g:if test="${flash.message}">
                    <div class="alert alert-info" role="alert">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="alert alert-danger" role="alert">${flash.error}</div>
                </g:if>
                <g:hasErrors bean="${veiculoInstance}">
                    <div class="alert alert-danger">
                        <g:renderErrors bean="${veiculoInstance}" as="list" />
                    </div>
                </g:hasErrors>

                <g:if test="${action==Util.ACTION_VIEW}">
                    <div class="tabbable">
                        <ul class="nav nav-tabs">
                            <li class="active" ><a href="#tab1" data-toggle="tab">${entityName}</a></li>
                            <g:if test="${veiculoInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA}">
                                <li><a href="#tab2" data-toggle="tab">Cartões</a></li>
                            </g:if>
                            <li><a href="#tab3" data-toggle="tab">Funcionários</a></li>
                            <li><a href="#tab4" data-toggle="tab">Hodômetro</a></li>

                            <g:if test="${veiculoInstance.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO &&
                                    veiculoInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA}">
                                <li><a href="#tab5" data-toggle="tab">Faturas</a></li>
                            </g:if>

                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab1">
                                <g:render template="basico" model="[veiculoInstance: veiculoInstance, unidadeInstance: unidadeInstance, tamMaxEmbossing: tamMaxEmbossing]"/>
                            </div>

                            <g:if test="${veiculoInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA}">
                                <div class="tab-pane" id="tab2">
                                    <g:render template="/cartao/cartoes" model="[portador: veiculoInstance.portador]"/>
                                </div>

                            </g:if>

                            <div class="tab-pane" id="tab3">
                                <g:render template="/maquinaMotorizada/funcionarios" model="${[instance: veiculoInstance, instanceName: "Veiculo"]}"/>
                            </div>
                            <div class="tab-pane" id="tab4">
                                <g:render template="hodometro"/>
                            </div>

                            <g:if test="${veiculoInstance.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO &&
                                    veiculoInstance.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA}">
                                <div class="tab-pane" id="tab5">
                                    <g:render template="/portadorCorte/faturas" model="${[portador: veiculoInstance.portador]}"/>
                                </div>
                            </g:if>


                        </div>
                    </div>
                </g:if>
                <g:else>
                    <g:render template="basico"/>
                </g:else>
            </div>
        </div>
    </body>
</html>
