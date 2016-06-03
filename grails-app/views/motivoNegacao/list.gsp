
<%@ page import="com.sysdata.gestaofrota.MotivoNegacao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'motivoNegacao.label', default: 'MotivoNegacao')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<br><br>
        <div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.list.label" args="[entityName]" /></h4>
			</div>
			<div class="panel-body">
				<a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
				<g:link action="create" class="btn btn-default">
					<span class="glyphicon glyphicon-plus"></span>
					<g:message code="default.new.label" args="[entityName]" />
				</g:link>
				<br><br>
				<div id="list-motivoNegacao" class="content scaffold-list" role="main">
					<g:if test="${flash.message}">
						<div class="message" role="status">${flash.message}</div>
					</g:if>
					<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
						<thead>
						<tr>

							<g:sortableColumn property="codigo" title="${message(code: 'motivoNegacao.codigo.label', default: 'Codigo')}" />

							<g:sortableColumn property="descricao" title="${message(code: 'motivoNegacao.descricao.label', default: 'Descricao')}" />

						</tr>
						</thead>
						<tbody>
						<g:each in="${motivoNegacaoInstanceList}" status="i" var="motivoNegacaoInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

								<td><g:link action="show" id="${motivoNegacaoInstance.id}">${fieldValue(bean: motivoNegacaoInstance, field: "codigo")}</g:link></td>

								<td>${fieldValue(bean: motivoNegacaoInstance, field: "descricao")}</td>

							</tr>
						</g:each>
						</tbody>
					</table>
					%{--<div class="paginateButtons">
						<g:paginate total="${motivoNegacaoInstanceTotal}" />
					</div>--}%
				</div>
			</div>
		</div>
	</body>
</html>
