
<%@ page import="br.com.acception.greport.ParameterReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'parameterReport.label', default: 'ParameterReport')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-parameterReport" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-parameterReport" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list parameterReport">
			
				<g:if test="${parameterReportInstance?.dataType}">
				<li class="fieldcontain">
					<span id="dataType-label" class="property-label"><g:message code="parameterReport.dataType.label" default="Data Type" /></span>
					
						<span class="property-value" aria-labelledby="dataType-label"><g:fieldValue bean="${parameterReportInstance}" field="dataType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${parameterReportInstance?.label}">
				<li class="fieldcontain">
					<span id="label-label" class="property-label"><g:message code="parameterReport.label.label" default="Label" /></span>
					
						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${parameterReportInstance}" field="label"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${parameterReportInstance?.markupType}">
				<li class="fieldcontain">
					<span id="markupType-label" class="property-label"><g:message code="parameterReport.markupType.label" default="Markup Type" /></span>
					
						<span class="property-value" aria-labelledby="markupType-label"><g:fieldValue bean="${parameterReportInstance}" field="markupType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${parameterReportInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="parameterReport.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${parameterReportInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${parameterReportInstance?.order}">
				<li class="fieldcontain">
					<span id="order-label" class="property-label"><g:message code="parameterReport.order.label" default="Order" /></span>
					
						<span class="property-value" aria-labelledby="order-label"><g:fieldValue bean="${parameterReportInstance}" field="order"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${parameterReportInstance?.report}">
				<li class="fieldcontain">
					<span id="report-label" class="property-label"><g:message code="parameterReport.report.label" default="Report" /></span>
					
						<span class="property-value" aria-labelledby="report-label"><g:link controller="report" action="show" id="${parameterReportInstance?.report?.id}">${parameterReportInstance?.report?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${parameterReportInstance?.id}" />
					<g:link class="edit" action="edit" id="${parameterReportInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
