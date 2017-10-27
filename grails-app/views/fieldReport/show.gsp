
<%@ page import="br.com.acception.greport.FieldReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'fieldReport.label', default: 'FieldReport')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	<br><br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.show.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<div class="nav" role="navigation">
					<a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
					<g:link class="btn btn-default" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
					<g:link class="btn btn-default" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>
			</div>
			<br><br>
			<div id="show-fieldReport" class="content scaffold-show" role="main">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				<ul class="properties">

					<g:if test="${fieldReportInstance?.dataType}">
						<li class="fieldcontain">
							<span id="dataType-label" class=" p-label"><g:message code="fieldReport.dataType.label" default="Data Type" /></span>

							<span class="p-value" aria-labelledby="dataType-label"><g:fieldValue bean="${fieldReportInstance}" field="dataType"/></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.groupBy}">
						<li class="fieldcontain">
							<span id="groupBy-label" class="property-label p-label"><g:message code="fieldReport.groupBy.label" default="Group By" /></span>

							<span class="property-value p-value" aria-labelledby="groupBy-label"><g:formatBoolean boolean="${fieldReportInstance?.groupBy}" /></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.label}">
						<li class="fieldcontain">
							<span id="label-label" class="property-label p-label"><g:message code="fieldReport.label.label" default="Label" /></span>

							<span class="property-value p-value" aria-labelledby="label-label"><g:fieldValue bean="${fieldReportInstance}" field="label"/></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.name}">
						<li class="fieldcontain">
							<span id="name-label" class="property-label p-label"><g:message code="fieldReport.name.label" default="Name" /></span>

							<span class="property-value p-value" aria-labelledby="name-label"><g:fieldValue bean="${fieldReportInstance}" field="name"/></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.order}">
						<li class="fieldcontain">
							<span id="order-label" class="property-label p-label"><g:message code="fieldReport.order.label" default="Order" /></span>

							<span class="property-value p-value" aria-labelledby="order-label"><g:fieldValue bean="${fieldReportInstance}" field="order"/></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.report}">
						<li class="fieldcontain">
							<span id="report-label" class="property-label p-label"><g:message code="fieldReport.report.label" default="Report" /></span>

							<span class="property-value p-value" aria-labelledby="report-label"><g:link controller="report" action="show" id="${fieldReportInstance?.report?.id}">${fieldReportInstance?.report?.encodeAsHTML()}</g:link></span>

						</li>
					</g:if>

					<g:if test="${fieldReportInstance?.totalizer}">
						<li class="fieldcontain">
							<span id="totalizer-label" class="property-label p-label"><g:message code="fieldReport.totalizer.label" default="Totalizer" /></span>

							<span class="property-value p-value" aria-labelledby="totalizer-label"><g:formatBoolean boolean="${fieldReportInstance?.totalizer}" /></span>

						</li>
					</g:if>

				</ul>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${fieldReportInstance?.id}" />
						<span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
						<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>
	</body>
</html>
