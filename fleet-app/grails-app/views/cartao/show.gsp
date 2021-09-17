<%@ page import="com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Cartao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'cartao.label', default: 'Cartao')}" />
		<title>Detalhes de Cartão</title>
	</head>
	<body>
		<div class="panel panel-default panel-top">
			<div class="panel-heading">
				<h4>Detalhes de Cartão</h4>
			</div>

			<div class="panel-body">

                <div class="buttons-top">
                    <a class="btn btn-default" href="${createLink(uri: '/')}">
                        <span class="glyphicon glyphicon-home"></span>
                        <g:message code="default.home.label"/>
                    </a>
                    <g:link class="btn btn-default" action="list">
                        <span class="glyphicon glyphicon-list"></span>
                        <g:message code="default.list.label" args="[entityName]"/>
                    </g:link>
                    <g:link class="btn btn-default" action="resetSenha" params="[id: cartaoInstance?.id]">
                        Reset Senha
                    </g:link>

                </div>

                <alert:all/>

				<div class="panel-top">
					<table class="table table-bordered">
						<tr>
							<th>#</th>
							<td>${cartaoInstance?.id}</td>
						</tr>
						<tr>
							<th>Data/Hora Criação</th>
							<td>${cartaoInstance?.dateCreated.format('dd/MM/yyyy HH:mm')}</td>
						</tr>
						<tr>
							<th>Número</th>
							<td>${cartaoInstance?.numero}</td>
						</tr>
						<tr>
							<th>Cliente</th>
							<td><g:link controller="rh" action="show" id="${cartaoInstance?.portador?.unidade?.rh?.id}">${cartaoInstance?.portador?.unidade?.rh?.nomeFantasia}</g:link></td>
						</tr>
						<tr>
							<th>Unidade</th>
							<td>${cartaoInstance?.portador?.unidade?.nome}</td>
						</tr>
						<tr>
							<th>Portador</th>
							<td><g:link controller="portadorCorte" action="showPortador" params="[prtId: cartaoInstance?.portador?.id]">${cartaoInstance?.portador.nomeEmbossing}</g:link></td>
						</tr>
						<tr>
							<th>Status</th>
							<td>${cartaoInstance?.status.nome}</td>
						</tr>
						<tr>
							<th>Data Validade</th>
							<td>${cartaoInstance?.validade.format('dd/MM/yyyy')}</td>
						</tr>

						<g:if test="${cartaoInstance?.motivoCancelamento}">
							<tr>
								<th>Motivo Cancelamento</th>
								<td>${cartaoInstance?.motivoCancelamento?.nome}</td>
							</tr>

						</g:if>

						<g:if test="${cartaoInstance?.portador?.limiteTotal}">
							<tr>
								<th>Limite de Crédito</th>
								<td>${ Util.formatCurrency(cartaoInstance?.portador?.limiteTotal)}</td>
							</tr>

						</g:if>

						<g:if test="${cartaoInstance?.portador?.saldoTotal}">
							<tr>
								<th>Saldo Disponível</th>
								<td>${Util.formatCurrency(cartaoInstance?.portador?.saldoTotal)}</td>
							</tr>

						</g:if>

						<g:if test="${cartaoInstance?.tipo}">
							<tr>
								<th>Tipo Cartão</th>
								<td>${cartaoInstance?.tipo?.name}</td>
							</tr>

						</g:if>
					</table>
				</div>

                <g:if test="${cartaoInstance.relacaoPortador.sort {it.dataInicio}}">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Histórico de Relações com Portadores
                        </div>
                        <div class="panel-body">
                            <table class="table">
                                <thead>
                                <th>Portador</th>
                                <th>Data Início</th>
                                <th>Data Fim</th>
                                <th>Status</th>
                                </thead>
                                <tbody>
                                <g:each in="${cartaoInstance.relacaoPortador.sort { it.dataInicio }}" var="relacao">
                                    <tr>
                                        <td>${relacao.portador.nomeEmbossing}</td>
                                        <td><g:formatDate date="${relacao.dataInicio}" format="dd/MM/yy HH:mm:ss"></g:formatDate></td>
                                        <td><g:formatDate date="${relacao.dataFim}" format="dd/MM/yy HH:mm:ss"></g:formatDate></td>
                                        <td>${relacao.status.nome}</td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </g:if>
            </div>
		</div>
	</body>
</html>
