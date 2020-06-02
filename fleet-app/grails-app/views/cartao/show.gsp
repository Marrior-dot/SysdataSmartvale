<%@ page import="com.sysdata.gestaofrota.Cartao" %>
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
							<th>Número</th>
							<td>${cartaoInstance?.numero}</td>
						</tr>
						<tr>
							<th>Portador</th>
							<td>${cartaoInstance?.portador.nomeEmbossing}</td>
						</tr>
						<tr>
							<th>Status</th>
							<td>${cartaoInstance?.status.nome}</td>
						</tr>
						<tr>
							<th>Data Validade</th>
							<td>${cartaoInstance?.validade.format('dd/MM/yyyy')}</td>
						</tr>
						<tr>
							<th>Data/Hora Criação</th>
							<td>${cartaoInstance?.dateCreated.format('dd/MM/yyyy HH:mm')}</td>
						</tr>

						<g:if test="${cartaoInstance?.motivoCancelamento}">
							<tr>
								<th>Motivo Cancelamento</th>
								<td>${cartaoInstance?.motivoCancelamento?.nome}</td>
							</tr>

						</g:if>
					</table>
				</div>


			</div>
		</div>
	</body>
</html>
