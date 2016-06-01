<%@ page import="com.sysdata.gestaofrota.Produto" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
	<br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.create.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
			<g:link class="btn btn-default" action="list"><span class="glyphicon glyphicon-list"></span> <g:message code="default.list.label" args="[entityName]" /></g:link>
			<br>
			<br>
			<div id="create-produto" class="content scaffold-create" role="main">

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
				<g:form action="save" >
					<fieldset class="form">
						<g:render template="form"/>
					</fieldset>
					<button type="submit" name="create" class="btn btn-default">
						<span class="glyphicon glyphicon-floppy-disk"></span>
						<g:message code="default.button.create.label" default="Create" />
					</button>
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
