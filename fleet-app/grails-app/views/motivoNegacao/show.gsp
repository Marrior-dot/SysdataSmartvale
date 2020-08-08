
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
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			<a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span><g:message code="default.home.label"/></a>
			<g:link class="btn btn-default" action="list"><span class="glyphicon glyphicon-list"></span><g:message code="default.list.label" args="[entityName]" /></g:link>

			<sec:ifAnyGranted roles="ROLE_PROC">
				<g:link class="btn btn-default" action="create"><span class="glyphicon glyphicon-plus"></span><g:message code="default.new.label" args="[entityName]" /></g:link>
			</sec:ifAnyGranted>

			<div class="panel-top">

				<table class="table table-bordered">
					<tbody>
					<tr>
						<th>#</th>
						<td>${motivoNegacaoInstance?.id}</td>
					</tr>
					<tr>
						<th>Código</th>
						<td>${motivoNegacaoInstance?.codigo}</td>
					</tr>
					<tr>
						<th>Descrição</th>
						<td>${motivoNegacaoInstance?.descricao}</td>
					</tr>
					</tbody>
				</table>

			</div>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${motivoNegacaoInstance?.id}" />
						<sec:ifAnyGranted roles="ROLE_PROC">
							<g:link class="btn btn-default" action="edit" id="${motivoNegacaoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
							<g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</sec:ifAnyGranted>
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
