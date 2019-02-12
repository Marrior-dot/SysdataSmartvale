
<%@ page import="com.sysdata.gestaofrota.Cartao" %>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="layout-restrito"/>
		<g:set var="entityName" value="Cartão"/>
		<title>Lista de Cartões</title>
	</head>
	<body>
		<br><br>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.list.label" args="[entityName]"/></h4>
			</div>

			<div class="panel-body">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="alert">${flash.message}</div>
				</g:if>
				<g:form>
					<div class="buttons">
						<a class="btn btn-default" href="${createLink(uri: '/')}"><span
								class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
						%{--<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-plus"></span>
							<g:message code="default.new.label" args="[entityName]"/>
						</g:link>--}%
					</div>
					<div id="list-cartao" class="content scaffold-list" role="main">
						<br><br>
						<table>
							<thead>
							<tr>

								<g:sortableColumn property="numero" title="${message(code: 'cartao.numero.label', default: 'Numero')}" />

								<g:sortableColumn property="status" title="${message(code: 'cartao.status.label', default: 'Status')}" />

								<g:sortableColumn property="motivoCancelamento" title="${message(code: 'cartao.motivoCancelamento.label', default: 'Motivo Cancelamento')}" />

								<th><g:message code="cartao.arquivo.label" default="Arquivo" /></th>

								<g:sortableColumn property="via" title="${message(code: 'cartao.via.label', default: 'Via')}" />

								<g:sortableColumn property="dateCreated" title="${message(code: 'cartao.dateCreated.label', default: 'Data Emissão')}" />

							</tr>
							</thead>
							<tbody>
							<g:each in="${cartaoInstanceList}" status="i" var="cartaoInstance">
								<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

									<td>${fieldValue(bean: cartaoInstance, field: "numero")}</td>

									<td>${fieldValue(bean: cartaoInstance, field: "status")}</td>

									<td>${fieldValue(bean: cartaoInstance, field: "motivoCancelamento")}</td>

									<td>${fieldValue(bean: cartaoInstance, field: "arquivo")}</td>

									<td>${fieldValue(bean: cartaoInstance, field: "via")}</td>

									<td><g:formatDate date="${cartaoInstance.dateCreated}" /></td>

								</tr>
							</g:each>
							</tbody>
						</table>
						<div class="pagination">
							<g:paginate total="${cartaoInstanceTotal}" />
						</div>
					</div>
				</g:form>
			</div>
		</div>
	</body>
</html>
