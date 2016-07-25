<%@ page import="com.sysdata.gestaofrota.Produto" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
	<br><br><br>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.edit.label" args="[entityName]" /></h4>
			</div>
			<div class="panel-body">
				<div class="nav" role="navigation">
					<a class="btn btn-default" href="${createLink(uri: '/')}">
						<span class="glyphicon glyphicon-home"></span>
						<g:message code="default.home.label"/>
					</a>
					<g:link class="btn btn-default" action="list">
						<span class="glyphicon glyphicon-list"></span>
						<g:message code="default.list.label" args="[entityName]" />
					</g:link>
					<g:link class="btn btn-default" action="create">
						<span class="glyphicon glyphicon-plus"></span>
						<g:message code="default.new.label" args="[entityName]" /></g:link>
				</div>
				<br><br><br>
				<div id="edit-produto" class="content scaffold-edit" role="main">
					<g:if test="${flash.message}">
						<div class="message" role="status">${flash.message}</div>
					</g:if>
					<g:hasErrors bean="${produtoInstance}">

						<div class="errors">
							<g:eachError bean="${produtoInstance}" var="error">
								<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
							</g:eachError>
						</div>

					</g:hasErrors>
					<g:form method="post" >
						<g:hiddenField name="id" value="${produtoInstance?.id}" />
						<g:hiddenField name="version" value="${produtoInstance?.version}" />
						<fieldset class="form">
							<g:render template="form"/>
						</fieldset>
						<fieldset class="buttons">
							<g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
							<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</div>
			</div>
		</div>

	</body>
</html>
