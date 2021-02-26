
<%@ page import="com.sysdata.gestaofrota.Veiculo; com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.TipoTransacao; com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
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

                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <th>#</th>
                        <td>${transacaoInstance.id}</td>
                    </tr>
                    <tr>
                        <th>Data/Hora Criação</th>
                        <td><g:formatDate date="${transacaoInstance?.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                    </tr>
                    <tr>
                        <th>Tipo</th>
                        <td>${transacaoInstance.tipo?.nome}</td>
                    </tr>
                    <tr>
                        <th>Status Processamento</th>
                        <td>${transacaoInstance?.status?.nome}</td>
                    </tr>
                    <tr>
                        <th>Status Rede</th>
                        <td>${transacaoInstance?.statusControle?.nome}</td>
                    </tr>
                    <tr>
                        <th>Valor</th>
                        <td><g:formatNumber number="${transacaoInstance.valor}" type="currency" currencySymbol="R\$ " format="#0.00" /></td>
                    </tr>
                    <tr>
                        <th>Funcionário</th>
                        <td>${transacaoInstance.participante?.nome}</td>
                    </tr>
                    <tr>
                        <th>Cartão</th>
                        <td>${transacaoInstance.cartao?.numero}</td>
                    </tr>
                    <tr>
                        <th>Estabelecimento</th>
                        <td><g:link controller="estabelecimento" action="show" id="${transacaoInstance.estabelecimento?.id}">${transacaoInstance.estabelecimento?.nome}</g:link></td>
                    </tr>
                    <tr>
                        <th>Terminal</th>
                        <td>${transacaoInstance.terminal}</td>
                    </tr>
                    <tr>
                        <th>NSU Terminal</th>
                        <td>${transacaoInstance.nsuTerminal}</td>
                    </tr>
                    <tr>
                        <th>NSU Autorizador</th>
                        <td>${transacaoInstance.nsu}</td>
                    </tr>
                    <tr>
                        <th>Código Retorno</th>
                        <td>${transacaoInstance.codigoRetorno}</td>
                    </tr>
                    <tr>
                        <th>Motivo Negação</th>
                        <td>${transacaoInstance.motivoNegacao}</td>
                    </tr>

                    <g:if test="${transacaoInstance.maquina?.instanceOf(Veiculo) && transacaoInstance.maquina?.placa}">
                        <tr>
                            <th>Veículo</th>
                            <td>${transacaoInstance.maquina?.placa}</td>
                        </tr>
                    </g:if>

                    <g:if test="${transacaoInstance.maquina?.instanceOf(Equipamento) && transacaoInstance.maquina?.codigo}">
                        <tr>
                            <th>Equipamento</th>
                            <td>${transacaoInstance.maquina?.codigo}</td>
                        </tr>
                    </g:if>

                    <tr>
                        <th>${transacaoInstance.tipo.nome}</th>
                        <td>${transacaoInstance.produtos*.produto?.nome}</td>
                    </tr>

                    <g:if test="${transacaoInstance.quilometragem}">
                        <tr>
                            <th>Hodômetro</th>
                            <td>${transacaoInstance.quilometragem}</td>
                        </tr>
                    </g:if>
                    <g:if test="${transacaoInstance.qtd_litros}">
                        <tr>
                            <th>Qtde abastecida (lts)</th>
                            <td>${transacaoInstance.qtd_litros}</td>
                        </tr>
                    </g:if>

                    </tbody>
                </table>

                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab1" data-toggle="tab">Lançamentos</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active">
                            <g:render template="lancamentos" model="[transacao: transacaoInstance]"></g:render>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>

    </body>
</html>
