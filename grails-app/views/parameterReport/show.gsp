
<%@ page import="br.com.acception.greport.ParameterReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'parameterReport.label', default: 'ParameterReport')}" />
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
			</div>
			<br><br>

			<div id="show-parameterReport" class="content scaffold-show" role="main">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				<ul class="properties">
					<g:if test="${parameterReportInstance?.report}">
						<li class="fieldcontain">
							<span class="property-label p-label">
								<g:message code="parameterReport.report.label" default="Report" />
							</span>
							<span class="property-value p-value" aria-labelledby="report-label">
								<g:link controller="report" action="show" id="${parameterReportInstance?.report?.id}">
									${parameterReportInstance?.report?.name?.encodeAsHTML()}
								</g:link>
							</span>
						</li>
					</g:if>

					<li class="fieldcontain">
						<span class="property-label p-label">
							<g:message code="parameterReport.mandatory.label" default="Mandatory" />
						</span>
						<span class="property-value p-value" aria-labelledby="report-label">
							${parameterReportInstance?.mandatory ? 'YES' : 'NO'}
						</span>
					</li>


					<li class="fieldcontain">
						<span class="property-label p-label">
							Roles
						</span>
						<span class="property-value p-value" aria-labelledby="report-label">
							<g:if test="${parameterReportInstance?.roles}">
								${parameterReportInstance?.roles?.sort{it.authority}*.authority?.join(', ')}
							</g:if>
							<g:else>
								(NENHUM)
							</g:else>
						</span>
					</li>

					<g:if test="${parameterReportInstance?.dataType}">
						<li class="fieldcontain">
							<span id="dataType-label" class="property-label p-label"><g:message code="parameterReport.dataType.label" default="Data Type" /></span>

							<span class="property-value p-value" aria-labelledby="dataType-label"><g:fieldValue bean="${parameterReportInstance}" field="dataType"/></span>

						</li>
					</g:if>

					<g:if test="${parameterReportInstance?.label}">
						<li class="fieldcontain">
							<span id="label-label" class="property-label p-label"><g:message code="parameterReport.label.label" default="Label" /></span>

							<span class="property-value p-value" aria-labelledby="label-label"><g:fieldValue bean="${parameterReportInstance}" field="label"/></span>

						</li>
					</g:if>

					<g:if test="${parameterReportInstance?.markupType}">
						<li class="fieldcontain">
							<span id="markupType-label" class="property-label p-label"><g:message code="parameterReport.markupType.label" default="Markup Type" /></span>

							<span class="property-value p-value" aria-labelledby="markupType-label"><g:fieldValue bean="${parameterReportInstance}" field="markupType"/></span>

						</li>
					</g:if>

					<g:if test="${parameterReportInstance?.name}">
						<li class="fieldcontain">
							<span id="name-label" class="property-label p-label"><g:message code="parameterReport.name.label" default="Name" /></span>

							<span class="property-value p-value" aria-labelledby="name-label"><g:fieldValue bean="${parameterReportInstance}" field="name"/></span>

						</li>
					</g:if>

					<g:if test="${parameterReportInstance?.order}">
						<li class="fieldcontain">
							<span id="order-label" class="property-label p-label"><g:message code="parameterReport.order.label" default="Order" /></span>

							<span class="property-value p-value" aria-labelledby="order-label"><g:fieldValue bean="${parameterReportInstance}" field="order"/></span>

						</li>
					</g:if>
				</ul>

				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${parameterReportInstance?.id}" />
						<span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
						<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>

	</body>
</html>
