
<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Transacao"%>
<%@ page import="com.sysdata.gestaofrota.StatusTransacao" %>
<%@ page import="com.sysdata.gestaofrota.TipoTransacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusControleAutorizacao" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="layout-restrito" />
    <g:set var="entityName" value="Transações Pendentes" />
    <g:javascript src="transacoesPendentes.js"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<br><br>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.list.label" args="[entityName]" /></h4>
    </div>
    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span
                    class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            <g:link class="btn btn-default" action="list">
                <span class="glyphicon glyphicon-plus"></span>
                <g:message code="default.list.label" args="[entityName]" />
            </g:link>
            <g:link class="btn btn-default" action="listPendentes">
                <span class="glyphicon glyphicon-list"></span>
                Lista de Transações Pendentes
            </g:link>
        </div>
        <br><br>
        <div class="body">
            <g:if test="${flash.error}">
                <div class="alert alert-danger" role="alert">${flash.error}</div>
            </g:if>
            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>

            <g:render template="filtro" model="[action: 'listPendentes']"/>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Transações</h3>
                </div>

                <div class="list">
                    <table class="table table-striped table-bordered table-condensed" style="font-size: 12px">
                        <thead>
                        <tr>
                            <th class="text-center"><g:checkBox name="geral" value="${false}"/></th>
                            <th>ID</th>
                            <th><g:message code="transacao.nsu.label" default="NSU" /></th>
                            <th>Data/Hora</th>
                            <th><g:message code="transacao.codigoEstabelecimento.label" default="Codigo Estabelecimento" /></th>
                            <th><g:message code="transacao.cartao.label" default="Cartao" /></th>
                            <th><g:message code="transacao.participante.label" default="Funcionário" /></th>
                            <th><g:message code="transacao.tipo.label" default="Tipo" /></th>
                            <th><g:message code="transacao.status.label" default="Status" /></th>
                            <th><g:message code="transacao.statusControle.label" default="Status Controle" /></th>
                            <th><g:message code="transacao.valor.label" default="Valor" /></th>
                            <sec:ifAnyGranted roles="ROLE_PROC">
                                <th class="text-center">Ações</th>
                            </sec:ifAnyGranted>
                        </tr>
                        </thead>
                        <tbody>
                        <g:form action="alterarStatus" name="desfazerConfirmarTransacoes">
                            <g:hiddenField name="tipoAlteracao"/>
                            <g:each in="${transacaoInstanceList}" var="transacao">
                                <tr>
                                    <td class="text-center">
                                        <input type="checkbox" name="transacoes" id="transacoes" value="${transacao.id}">
                                    </td>
                                    <td><g:link action="show" id="${transacao.id}">${fieldValue(bean: transacao, field: "id")}</g:link></td>
                                    <td>${transacao.nsu}</td>
                                    <td><g:formatDate date="${transacao.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                                    <td>${transacao.codigoEstabelecimento}</td>
                                    <td>${Util.maskCard(transacao.numeroCartao)}</td>
                                    <td>${transacao.participante?.nome}</td>
                                    <g:if test="${transacao.tipo.nome == "Cancelamento"}">
                                        <td>${transacao.tipo.nome}¹</td>
                                    </g:if>
                                    <g:else>
                                        <td>${transacao.tipo.nome}</td>
                                    </g:else>
                                    <td>${transacao.status.nome}</td>
                                    <td>${transacao.statusControle?.nome}</td>
                                    <td><g:formatNumber number="${transacao.valor}" format="#0.00" /></td>

                                    <sec:ifAnyGranted roles="ROLE_PROC">
                                        <td>
                                            <g:if test="${transacao.estornavel}">
                                                <span title="Estornar">
                                                    <g:link class="undo" action="estornar" id="${transacao.id}"></g:link>
                                                </span>
                                            </g:if>
                                        </td>
                                    </sec:ifAnyGranted>
                                </tr>
                            </g:each>
                        </g:form>
                        </tbody>
                    </table>
                </div>

                <div class="panel-body">
                    <g:paginate total="${transacaoInstanceTotal}" params="${params}"/>
                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default" form="desfazerConfirmarTransacoes"
                            onclick="$('input:hidden#tipoAlteracao').val('desfazimento');return confirm('Tem certeza que deseja desfazer as transações selecionadas?');">
                        <span class="glyphicon glyphicon-remove-sign"></span> Desfazer Selecionados
                    </button>

                    <button type="submit" class="btn btn-default" form="desfazerConfirmarTransacoes"
                            onclick="$('input:hidden#tipoAlteracao').val('confirmacao');return confirm('Tem certeza que deseja confirmar as transações selecionadas?');">
                        <span class="glyphicon glyphicon-ok-sign"></span> Confirmar Selecionados
                    </button>
                    <hr>
                    <strong>¹Obs: Não é possível confirmar transações de cancelamento no momento.</strong>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
