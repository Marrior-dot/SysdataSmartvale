<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'tipoEquipamento.label', default: 'Tipo de Equipamento')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
	<br><br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.create.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<div class="nav">
				<a class="btn btn-default" href="${createLink(uri: '/')}">
					<span class="glyphicon glyphicon-home"></span>
					<g:message code="default.home.label"/></a>
				<g:link class="btn btn-default" action="list">
					<span class="glyphicon glyphicon-list"></span>
					<g:message code="default.list.label" args="[entityName]" /></g:link>
			</div>
			<br><br>
			<div id="create-tipoEquipamento" class="content scaffold-create" role="main">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				<g:hasErrors bean="${tipoEquipamentoInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${tipoEquipamentoInstance}" var="error">
							<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
						</g:eachError>
					</ul>
				</g:hasErrors>
				<g:form action="save" >
					<g:render template="form" model="[tipoEquipamentoInstance: tipoEquipamentoInstance]"/>
					<g:submitButton name="create" class="btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
