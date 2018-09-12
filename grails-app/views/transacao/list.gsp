<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusTransacao" %>
<%@ page import="com.sysdata.gestaofrota.TipoTransacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusControleAutorizacao" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'table-javascript-style.css')}"/>

</head>

<body>
<br><br>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.list.label" args="[entityName]"/></h4>
    </div>

    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span
                    class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            <g:link class="btn btn-default" action="list">
                <span class="glyphicon glyphicon-list"></span>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_PROC">
                <g:link class="btn btn-default" action="agendarAll">
                    <span class="glyphicon glyphicon-dashboard"></span>
                    Agendar Todas
                </g:link>
            %{--<g:link class="btn btn-default" action="simulador">
                <span class="glyphicon glyphicon-facetime-video"></span>
                Simulação de Transações
            </g:link> --}%
            </sec:ifAnyGranted>
            <br><br>
        </div>

        <div class="body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.errors}">
                <div class="errors">
                    <span style="font-weight:bold;padding-left:10px">ERROS</span>
                    <ul>
                        <g:each in="${flash.errors}" var="err">
                            <li>${err}</li>
                        </g:each>
                    </ul>
                </div>
            </g:if>

            <div class="panel panel-default">
                <div class="panel-heading">
                    Pesquisa por filtro
                </div>

                <div class="panel-body">
                    <g:form action="list">
                        <div class="row">
                            <div class="form-group col-md-2">
                                <label for="dataInicial">Dt Inicial</label>
                                <input type="text" name="dataInicial" id="dataInicial" class="form-control datepicker"
                                       value="${params.dataInicial}">
                            </div>

                            <div class="form-group col-md-2">
                                <label for="dataFinal">Dt Final</label>
                                <input type="text" name="dataFinal" id="dataFinal" class="form-control datepicker"
                                       value="${params.dataFinal}">
                            </div>

                            <div class="form-group col-md-3">
                                <label for="cartao">Cartão</label>
                                <input type="text" name="cartao" id="cartao" class="form-control" value="${params.cartao}">
                            </div>

                            <div class="form-group col-md-2">
                                <label for="codEstab">Cod Estab</label>
                                <input type="text" name="codEstab" id="codEstab" class="form-control"
                                       value="${params.codEstab}">
                            </div>

                            <div class="form-group col-md-2">
                                <label for="nsu">NSU</label>
                                <input type="number" min="0" name="nsu" id="nsu" class="form-control" value="${params.nsu}">
                            </div>
                        </div>

                        <div class="text-right">
                            <button type="submit" class="btn btn-default">
                                <i class="glyphicon glyphicon-search"></i> Listar
                            </button>
                        </div>
                    </g:form>
                </div>
            </div>

            <table class="table table-striped table-bordered table-condensed" style="font-size: 12px">
                <thead>
                <th>ID</th>
                <th><g:message code="transacao.nsu.label" default="NSU"/></th>
                <th>Data/Hora</th>
                <th><g:message code="transacao.codigoEstabelecimento.label" default="Codigo Estabelecimento"/></th>
                <th><g:message code="transacao.cartao.label" default="Cartao"/></th>
                <th><g:message code="transacao.participante.label" default="Funcionário"/></th>
                <th><g:message code="transacao.tipo.label" default="Tipo"/></th>
                <th><g:message code="transacao.status.label" default="Status"/></th>
                <th><g:message code="transacao.statusControle.label" default="Status Controle"/></th>
                <th><g:message code="transacao.valor.label" default="Valor"/></th>
                <sec:ifAnyGranted roles="ROLE_PROC">
                    <th class="text-center">Ações</th>
                </sec:ifAnyGranted>
                </thead>

                <tbody>
                <g:each in="${transacaoInstanceList}" var="transacaoInstance">
                    <tr>
                        <td>
                            <g:link action="show" id="${transacaoInstance.id}">
                                ${fieldValue(bean: transacaoInstance, field: "id")}
                            </g:link>
                        </td>
                        <td>${transacaoInstance.nsu}</td>
                        <td>
                            <g:formatDate date="${transacaoInstance.dateCreated}" format="dd/MM/yyyy HH:mm:ss"/>
                        </td>
                        <td>${transacaoInstance.codigoEstabelecimento}</td>
                        <td>${transacaoInstance.numeroCartao ? Util.maskCard(transacaoInstance.numeroCartao) : '---'}</td>
                        <td>${transacaoInstance.participante?.nome}</td>
                        <td>${transacaoInstance?.tipo?.nome}</td>
                        <td>${transacaoInstance.status?.nome}</td>
                        <td>${transacaoInstance.statusControle?.nome}</td>
                        <td><g:formatNumber number="${transacaoInstance.valor}" format="#0.00"/></td>

                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <td class="text-center">
                                <g:if test="${transacaoInstance.status in [StatusTransacao.AGENDAR, StatusTransacao.AGENDADA] &&
                                        transacaoInstance.tipo in [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS] &&
                                        transacaoInstance.statusControle == StatusControleAutorizacao.CONFIRMADA}">
                                    <a class="btn btn-default btn-sm" title="Estornar" href="${createLink(action: 'estornar', id: transacaoInstance.id)}">
                                        <i class="glyphicon glyphicon-repeat"></i>
                                    </a>
                                </g:if>
                            </td>
                        </sec:ifAnyGranted>
                    </tr>
                </g:each>
                </tbody>
            </table>

            <div class="paginateButtons">
                <g:paginate total="${transacaoInstanceTotal}" params="${params}"/>
            </div>
        </div>
    </div>
</div>

</body>
</html>
