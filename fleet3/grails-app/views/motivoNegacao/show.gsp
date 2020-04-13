
<%@ page import="com.sysdata.gestaofrota.MotivoNegacao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'motivoNegacao.label', default: 'Motivo Negação')}" />
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
					<g:message code="default.home.label"/></a>
				<g:link class="btn btn-default" action="list">
					<span class="glyphicon glyphicon-list"></span>
					<g:message code="default.list.label" args="[entityName]" /></g:link>
				<g:link class="btn btn-default" action="create">
					<span class="glyphicon glyphicon-plus"></span>
					<g:message code="default.new.label" args="[entityName]" /></g:link>
			</div>
			<br><br>
			<div id="show-motivoNegacao" class="content scaffold-show" role="main">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				<ul class="properties">

					<g:if test="${motivoNegacaoInstance?.codigo}">
						<li class="fieldcontain">
							<span id="codigo-label" class="property-label p-label"><g:message code="motivoNegacao.codigo.label" default="Codigo" /></span>

							<span class="property-value p-value" aria-labelledby="codigo-label"><g:fieldValue bean="${motivoNegacaoInstance}" field="codigo"/></span>

						</li>
					</g:if>

					<g:if test="${motivoNegacaoInstance?.ocorrencia}">
						<li class="fieldcontain">
							<span id="descricao-label" class="property-label p-label"><g:message code="motivoNegacao.descricao.label" default="Descricao" /></span>

							<span class="property-value p-value" aria-labelledby="descricao-label"><g:fieldValue bean="${motivoNegacaoInstance}" field="ocorrencia"/></span>

						</li>
					</g:if>

				</ul>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${motivoNegacaoInstance?.id}" />
						<g:link class="btn btn-default" action="edit" id="${motivoNegacaoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
