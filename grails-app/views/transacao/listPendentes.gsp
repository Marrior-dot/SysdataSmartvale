
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusTransacao" %>
<%@ page import="com.sysdata.gestaofrota.TipoTransacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusControleAutorizacao" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
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

            <g:form action="listPendentes">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>Pesquisa por filtro</h4>
                    </div>
                    <div class="panel-body">
                        <fieldset class="search">

                            <label>Cartão: <g:textField class="form-control" name="cartao" value="${params.cartao}"/></label>
                            <label>Cod.Estab: <g:textField class="form-control" name="codEstab" value="${params.codEstab}"/></label>
                            <label>NSU: <g:textField class="form-control" name="nsu" value="${params.nsu}"/></label>
                        </fieldset>

                        <div class="buttons">
                            <g:submitButton class="list btn btn-default" name="list" value="Listar" />
                            <g:actionSubmit class="desfazer btn btn-default" action="desfazer" name="desfazer" value="Desfazer Selecionados"
                                            onclick="return confirm('Tem certeza que deseja desfazer as transações selecionadas?');">Desfazer</g:actionSubmit>
                            <g:actionSubmit class="confirmar btn btn-default" action="confirmar" name="confirmar" value="Confirmar Selecionados"
                                            onclick="return confirm('Tem certeza que deseja confirmar as transações selecionadas?');"></g:actionSubmit>
                        </div>
                    </div>
                </div>


                <div class="list">
                    <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                        <thead>
                        <tr>
                            <th><g:checkBox name="geral" value="${false}"/></th>
                            %{--<g:sortableColumn property="id" title="ID" />
                            <th><g:message code="transacao.nsu.label" default="" /></th>
                            <g:sortableColumn property="dateCreated" title="Data/Hora" />
                            <th><g:message code="transacao.codigoEstabelecimento.label" default="" /></th>
                            <th><g:message code="transacao.cartao.label" default="" /></th>
                            <th><g:message code="transacao.participante.label" default="Funcionário" /></th>
                            <g:sortableColumn property="tipo" title="${message(code: 'transacao.tipo.label', default: '')}" />
                            <g:sortableColumn property="status" title="${message(code: 'transacao.status.label', default: '')}" />
                            <g:sortableColumn property="statusControle" title="${message(code: 'transacao.statusControle.label', default: '')}" />
                            <g:sortableColumn property="valor" title="${message(code: 'transacao.valor.label', default: '')}" />

                            <sec:ifAnyGranted roles="ROLE_PROC">
                                <th>Ações</th>
                            </sec:ifAnyGranted>--}%

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
                                <th>Ações</th>
                            </sec:ifAnyGranted>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${transacaoInstanceList}" status="i" var="transacaoInstance">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                <td>
                                    <g:hiddenField name="transacoes" value="${transacaoInstance.id}"/>
                                    <g:checkBox name="selecionado${transacaoInstance.id}"/>
                                </td>
                                <td><g:link action="show" id="${transacaoInstance.id}">${fieldValue(bean: transacaoInstance, field: "id")}</g:link></td>
                                <td>${transacaoInstance.nsu}</td>
                                <td><g:formatDate date="${transacaoInstance.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                                <td>${transacaoInstance.codigoEstabelecimento}</td>
                                <td>${transacaoInstance.numeroCartao}</td>
                                <td>${transacaoInstance.participante?.nome}</td>
                                <g:if test="${transacaoInstance.tipo.nome == "Cancelamento"}">
                                    <td>${transacaoInstance.tipo.nome}¹</td>
                                </g:if>
                                <g:else>
                                    <td>${transacaoInstance.tipo.nome}</td>
                                </g:else>
                                <td>${transacaoInstance.status.nome}</td>
                                <td>${transacaoInstance.statusControle?.nome}</td>
                                <td><g:formatNumber number="${transacaoInstance.valor}" format="#0.00" /></td>

                                <sec:ifAnyGranted roles="ROLE_PROC">
                                    <td>
                                        <g:if test="${transacaoInstance.status in [StatusTransacao.AGENDAR,StatusTransacao.AGENDADA] &&
                                                transacaoInstance.tipo in [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS] &&
                                                transacaoInstance.statusControle==StatusControleAutorizacao.CONFIRMADA}">

                                            <span title="Estornar"><g:link class="undo" action="estornar" id="${transacaoInstance.id}"></g:link></span>

                                        </g:if>
                                    </td>
                                </sec:ifAnyGranted>



                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="paginateButtons">
                    <g:paginate total="${transacaoInstanceTotal}" params="${params}"/>

                </div>
                <br/>
                ¹Obs: Não é possível confirmar transações de cancelamento no momento.
                <div>

                </div>

            </g:form>
        </div>
    </div>
</div>
</body>
</html>
