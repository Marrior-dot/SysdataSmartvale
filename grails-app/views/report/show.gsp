
<%@ page import="br.com.acception.greport.Report" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
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
				<div id="show-report" class="content scaffold-show" role="main">
					<g:if test="${flash.message}">
						<div class="alert alert-info" role="status">${flash.message}</div>
					</g:if>
					<ol class="property-list report">



						<g:if test="${reportInstance?.queryType}">
							<div class="panel panel-default">
								<div class="panel-body">
									<strong><span id="queryType-label" class="property-label"><g:message code="report.queryType.label" default="Query Type" /></span></strong>

									<span class="property-value" aria-labelledby="queryType-label"><p class="relatorio-show"><g:fieldValue bean="${reportInstance}" field="queryType"/></p></span>
								</div>
							</div>
						</g:if>



						<g:if test="${reportInstance?.query}">
							<div class="panel panel-default">
								<div class="panel-body">
									<strong><span id="query-label" class="property-label"><g:message code="report.query.label" default="Query" /></span></strong>

									<span class="property-value" aria-labelledby="query-label"><p><g:fieldValue bean="${reportInstance}" field="query"/></p></span>
								</div>
							</div>
						</g:if>


						<g:if test="${reportInstance?.countQuery}">
							<div class="panel panel-default">
								<div class="panel-body">
									<strong><span id="countQuery-label" class="property-label"><g:message code="report.countQuery.label" default="Count Query" /></span></strong>
									<span class="property-value" aria-labelledby="countQuery-label"><p><g:fieldValue bean="${reportInstance}" field="countQuery"/></p></span>
								</div>
							</div>
						</g:if>


						<g:if test="${reportInstance?.fields}">
							<div class="panel panel-default">
								<div class="panel-body">
									<strong><span id="fields-label" class="property-label"><g:message code="report.fields.label" default="Fields" /></span></strong>
									<p>
										<g:each in="${reportInstance.fields}" var="f">
											<span class="property-value" aria-labelledby="fields-label"><g:link controller="fieldReport" action="show" id="${f.id}">${f?.id +" - "+f?.label} |</g:link></span>
										</g:each>
									</p>
								</div>
							</div>
						</g:if>


						<g:if test="${reportInstance?.parameters}">
							<div class="panel panel-default">
								<div class="panel-body">
									<strong><span id="parameters-label" class="property-label"><g:message code="report.parameters.label" default="Parameters" /></span></strong>
									<p>
										<g:each in="${reportInstance.parameters}" var="p">
											<span class="property-value" aria-labelledby="parameters-label"><g:link controller="parameterReport" action="show" id="${p.id}">${p?.id +" - "+p?.label} |</g:link></span>
										</g:each>
									</p>
								</div>
							</div>
						</g:if>


					</ol>
					<g:form>
						<fieldset class="buttons">
							<g:hiddenField name="id" value="${reportInstance?.id}" />
							<span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
							<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</div>
			</div>
		</div>

	</body>
</html>
