<%@ page import="br.com.acception.greport.ParameterReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'parameterReport.label', default: 'ParameterReport')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
	<br><br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.create.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<div class="nav" role="navigation">

				<a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
				<g:link class="btn btn-default"  action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>

			</div>
			<br><br>
			<div id="create-parameterReport" class="content scaffold-create" role="main">
				<g:if test="${flash.message}">
					<div class="message" role="status">${flash.message}</div>
				</g:if>
				<g:hasErrors bean="${parameterReportInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${parameterReportInstance}" var="error">
							<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
						</g:eachError>
					</ul>
				</g:hasErrors>
				<g:form action="save" >
					<fieldset class="form">
						<g:render template="form" model="${[parameterReportInstance: parameterReportInstance]}"/>
					</fieldset>
					<fieldset class="buttons">
						<g:submitButton name="create" class="btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
