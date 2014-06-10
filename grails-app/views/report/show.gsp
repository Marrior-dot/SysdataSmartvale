
<%@ page import="br.com.acception.greport.Report" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-report" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-report" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list report">
			
				<g:if test="${reportInstance?.countQuery}">
				<li class="fieldcontain">
					<span id="countQuery-label" class="property-label"><g:message code="report.countQuery.label" default="Count Query" /></span>
					
						<span class="property-value" aria-labelledby="countQuery-label"><g:fieldValue bean="${reportInstance}" field="countQuery"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reportInstance?.fields}">
				<li class="fieldcontain">
					<span id="fields-label" class="property-label"><g:message code="report.fields.label" default="Fields" /></span>
					
						<g:each in="${reportInstance.fields}" var="f">
						<span class="property-value" aria-labelledby="fields-label"><g:link controller="fieldReport" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${reportInstance?.parameters}">
				<li class="fieldcontain">
					<span id="parameters-label" class="property-label"><g:message code="report.parameters.label" default="Parameters" /></span>
					
						<g:each in="${reportInstance.parameters}" var="p">
						<span class="property-value" aria-labelledby="parameters-label"><g:link controller="parameterReport" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${reportInstance?.query}">
				<li class="fieldcontain">
					<span id="query-label" class="property-label"><g:message code="report.query.label" default="Query" /></span>
					
						<span class="property-value" aria-labelledby="query-label"><g:fieldValue bean="${reportInstance}" field="query"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reportInstance?.queryType}">
				<li class="fieldcontain">
					<span id="queryType-label" class="property-label"><g:message code="report.queryType.label" default="Query Type" /></span>
					
						<span class="property-value" aria-labelledby="queryType-label"><g:fieldValue bean="${reportInstance}" field="queryType"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${reportInstance?.id}" />
					<g:link class="edit" action="edit" id="${reportInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
