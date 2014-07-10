
<%@ page import="br.com.acception.greport.Report" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
				<span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
				<span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
			</ul>
		</div>
		<div id="show-report" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list report">
								
				<br />
				<hr>
				<br />
				
				<g:if test="${reportInstance?.queryType}">
				<li class="fieldcontain">
					<span id="queryType-label" class="property-label"><g:message code="report.queryType.label" default="Query Type" /></span>
					
						<span class="property-value" aria-labelledby="queryType-label"><p class="relatorio-show"><g:fieldValue bean="${reportInstance}" field="queryType"/></p></span>
					
				</li>
				</g:if>
				
				<br />
				<hr>
				<br />
				
				<g:if test="${reportInstance?.query}">
				<li class="fieldcontain">
					<span id="query-label" class="property-label"><g:message code="report.query.label" default="Query" /></span>
					
						<span class="property-value" aria-labelledby="query-label"><p><g:fieldValue bean="${reportInstance}" field="query"/></p></span>
					
				</li>
				</g:if>
				
				<br />
				<hr>
				<br />
			
				<g:if test="${reportInstance?.countQuery}">
				<li class="fieldcontain">
					<span id="countQuery-label" class="property-label"><g:message code="report.countQuery.label" default="Count Query" /></span>
					
						<span class="property-value" aria-labelledby="countQuery-label"><p><g:fieldValue bean="${reportInstance}" field="countQuery"/></p></span>
					
				</li>
				</g:if>
				
				<br />
				<hr>
				<br />
			
				<g:if test="${reportInstance?.fields}">
				<li class="fieldcontain">
					<span id="fields-label" class="property-label"><g:message code="report.fields.label" default="Fields" /></span>
						<p>
						<g:each in="${reportInstance.fields}" var="f">
						<span class="property-value" aria-labelledby="fields-label"><g:link controller="fieldReport" action="show" id="${f.id}">${f?.id +" - "+f?.label} |</g:link></span>
						</g:each>
						</p>
					
				</li>
				</g:if>
				
				<br />
				<hr>
				<br />
			
				<g:if test="${reportInstance?.parameters}">
				<li class="fieldcontain">
					<span id="parameters-label" class="property-label"><g:message code="report.parameters.label" default="Parameters" /></span>
						<p>
						<g:each in="${reportInstance.parameters}" var="p">
						<span class="property-value" aria-labelledby="parameters-label"><g:link controller="parameterReport" action="show" id="${p.id}">${p?.id +" - "+p?.label} |</g:link></span>
						</g:each>
						</p>
					
				</li>
				</g:if>
				
				<br />
				<hr>
				<br />
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${reportInstance?.id}" />
					<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
