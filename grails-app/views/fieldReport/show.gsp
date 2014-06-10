
<%@ page import="br.com.acception.greport.FieldReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fieldReport.label', default: 'FieldReport')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-fieldReport" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-fieldReport" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list fieldReport">
			
				<g:if test="${fieldReportInstance?.dataType}">
				<li class="fieldcontain">
					<span id="dataType-label" class="property-label"><g:message code="fieldReport.dataType.label" default="Data Type" /></span>
					
						<span class="property-value" aria-labelledby="dataType-label"><g:fieldValue bean="${fieldReportInstance}" field="dataType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.groupBy}">
				<li class="fieldcontain">
					<span id="groupBy-label" class="property-label"><g:message code="fieldReport.groupBy.label" default="Group By" /></span>
					
						<span class="property-value" aria-labelledby="groupBy-label"><g:formatBoolean boolean="${fieldReportInstance?.groupBy}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.label}">
				<li class="fieldcontain">
					<span id="label-label" class="property-label"><g:message code="fieldReport.label.label" default="Label" /></span>
					
						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${fieldReportInstance}" field="label"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="fieldReport.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${fieldReportInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.order}">
				<li class="fieldcontain">
					<span id="order-label" class="property-label"><g:message code="fieldReport.order.label" default="Order" /></span>
					
						<span class="property-value" aria-labelledby="order-label"><g:fieldValue bean="${fieldReportInstance}" field="order"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.report}">
				<li class="fieldcontain">
					<span id="report-label" class="property-label"><g:message code="fieldReport.report.label" default="Report" /></span>
					
						<span class="property-value" aria-labelledby="report-label"><g:link controller="report" action="show" id="${fieldReportInstance?.report?.id}">${fieldReportInstance?.report?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${fieldReportInstance?.totalizer}">
				<li class="fieldcontain">
					<span id="totalizer-label" class="property-label"><g:message code="fieldReport.totalizer.label" default="Totalizer" /></span>
					
						<span class="property-value" aria-labelledby="totalizer-label"><g:formatBoolean boolean="${fieldReportInstance?.totalizer}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${fieldReportInstance?.id}" />
					<g:link class="edit" action="edit" id="${fieldReportInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
