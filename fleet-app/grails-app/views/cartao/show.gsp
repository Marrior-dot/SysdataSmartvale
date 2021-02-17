<%@ page import="com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Cartao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'cartao.label', default: 'Cartao')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="panel panel-default panel-top">
			<div class="panel-heading">
				<h4>Cartão</h4>
			</div>

			<div class="panel-body">

				<a class="btn btn-default" href="${createLink(uri: '/')}">
					<span class="glyphicon glyphicon-home"></span>
					<g:message code="default.home.label"/>
				</a>
				<g:link class="btn btn-default" action="list">
					<span class="glyphicon glyphicon-list"></span>
					<g:message code="default.list.label" args="[entityName]"/>
				</g:link>


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


					</table>
				</div>


			</div>
		</div>
	</body>
</html>
