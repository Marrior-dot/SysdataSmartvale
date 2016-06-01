
<%@ page import="com.sysdata.gestaofrota.Produto" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		%{--<div class="nav" role="navigation">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
			<span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
		</div>--}%
	<br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.list.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			<div class="nav" role="navigation">
				<a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
				<g:link class="btn btn-default" action="create"><span class="glyphicon glyphicon-plus"></span> <g:message code="default.new.label" args="[entityName]" /></g:link>
			</div>
			<br>
			<br>
			<table class="table table-striped table-bordered table-hover table-condensed">
				<thead>
				<tr>

					<g:sortableColumn property="codigo" title="${message(code: 'produto.codigo.label', default: 'Codigo')}" />

					<g:sortableColumn property="nome" title="${message(code: 'produto.nome.label', default: 'Nome')}" />

					<g:sortableColumn property="tipo" title="${message(code: 'produto.tipo.label', default: 'Tipo')}" />

				</tr>
				</thead>
				<tbody>
				<g:each in="${produtoInstanceList}" status="i" var="produtoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link action="show" id="${produtoInstance.id}">${fieldValue(bean: produtoInstance, field: "codigo")}</g:link></td>

						<td>${fieldValue(bean: produtoInstance, field: "nome")}</td>

						<td>${fieldValue(bean: produtoInstance, field: "tipo")}</td>

					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${produtoInstanceTotal}" />
			</div>
		</div>
	</div>
	</body>
</html>
