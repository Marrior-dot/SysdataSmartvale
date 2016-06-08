
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
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
                    <g:message code="default.home.label"/>
                </a>
                <g:link class="btn btn-default" action="list">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]" />
                </g:link>
                <g:link class="btn btn-default" action="listPendentes">
                    <span class="glyphicon glyphicon-list"></span>
                    Lista de Transações Pendentes
                </g:link>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <ul class="properties">
                    <li>
                        <span class="p-label"><g:message code="transacao.id.label" default="Id" /></span>
                        <span class="p-value">${fieldValue(bean: transacaoInstance, field: "id")}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.dateCreated.label" default="Date Created" /></span>
                        <span class="p-value"><g:formatDate date="${transacaoInstance?.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.tipo.label" default="Tipo de Transação" /></span>
                        <span class="p-value">${transacaoInstance.tipo?.nome}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.status.label" default="Status" /></span>
                        <span class="p-value">${transacaoInstance?.statusControle?.nome}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.statusControle.label" default="Status Controle" /></span>
                        <span class="p-value">${transacaoInstance?.statusControle?.nome}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.valor.label" default="Valor"/></span>
                        <span class="p-value"><g:formatNumber number="${transacaoInstance.valor}" type="currency" currencySymbol="R\$ " format="#0.00" /></span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.participante.label" default="Participante" /></span>
                        <span class="p-value">${transacaoInstance.participante?.nome}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.cartao.label" default="Cartão" /></span>
                        <span class="p-value">${transacaoInstance.numeroCartao}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.codigoEstabelecimento.label" default="Código Estabelecimento" /></span>
                        <span class="p-value">${transacaoInstance.codigoEstabelecimento}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.terminal.label" default="Terminal" /></span>
                        <span class="p-value">${transacaoInstance.terminal}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.nsuTerminal.label" default="NSU Terminal" /></span>
                        <span class="p-value">${transacaoInstance.nsuTerminal}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.nsu.label" default="NSU" /></span>
                        <span class="p-value">${transacaoInstance.nsu}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.codigoRetorno.label" default="Código de Retorno" /></span>
                        <span class="p-value">${transacaoInstance.codigoRetorno}</span>
                    </li>
                    <g:if test="${transacaoInstance.maquina?.instanceOf(com.sysdata.gestaofrota.Veiculo)  }">
                        <li>
                            <span class="p-label"><g:message code="transacao.veiculo.label" default="Veículo" /></span>
                            <span class="p-value">${transacaoInstance.maquina?.placa}</span>
                        </li>
                    </g:if>
                    <g:if test="${transacaoInstance.maquina?.instanceOf(com.sysdata.gestaofrota.Equipamento)  }">
                        <li>
                            <span class="p-label"><g:message code="transacao.equipamento.label" default="Equipamento" /></span>
                            <span class="p-value">${transacaoInstance.maquina?.codigo}</span>
                        </li>
                    </g:if>
                    <li>
                        <span class="p-label"><g:message code="transacao.combustivel.label" default="Combustível" /></span>
                        <span class="p-value">${transacaoInstance.combustivel?.nome}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.quilometragem.label" default="Quilometragem" /></span>
                        <span class="p-value">${transacaoInstance.quilometragem}</span>
                    </li>
                    <li>
                        <span class="p-label"><g:message code="transacao.motivoNegacao.label" default="Motivo Negação" /></span>
                        <span class="p-value">${transacaoInstance.motivoNegacao?.codigo}-${transacaoInstance.motivoNegacao?.descricao}</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    </body>
</html>
