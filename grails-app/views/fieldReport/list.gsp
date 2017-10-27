
<%@ page import="br.com.acception.greport.FieldReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'fieldReport.label', default: 'FieldReport')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	<br><br>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.list.label" args="[entityName]" /></h4>
			</div>
			<div class="panel-body">
				<div class="nav" role="navigation">

						<a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
						<g:link class="btn btn-default" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>

				</div>
				<br><br>
				<div id="list-fieldReport" class="content scaffold-list" role="main">
					<g:if test="${flash.message}">
						<div class="alert alert-info" role="status">${flash.message}</div>
					</g:if>
					<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
						<thead>
						<tr>

							<g:sortableColumn property="dataType" title="${message(code: 'fieldReport.dataType.label', default: 'Data Type')}" />

							<g:sortableColumn property="groupBy" title="${message(code: 'fieldReport.groupBy.label', default: 'Group By')}" />

							<g:sortableColumn property="label" title="${message(code: 'fieldReport.label.label', default: 'Label')}" />

							<g:sortableColumn property="name" title="${message(code: 'fieldReport.name.label', default: 'Name')}" />

							<g:sortableColumn property="order" title="${message(code: 'fieldReport.order.label', default: 'Order')}" />

							<th><g:message code="fieldReport.report.label" default="Report" /></th>

						</tr>
						</thead>
						<tbody>
						<g:each in="${fieldReportInstanceList}" status="i" var="fieldReportInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

								<td><g:link action="show" id="${fieldReportInstance.id}">${fieldValue(bean: fieldReportInstance, field: "id")}</g:link></td>

								<td><g:formatBoolean boolean="${fieldReportInstance.groupBy}" /></td>

								<td>${fieldValue(bean: fieldReportInstance, field: "label")}</td>

								<td>${fieldValue(bean: fieldReportInstance, field: "name")}</td>

								<td>${fieldValue(bean: fieldReportInstance, field: "order")}</td>

								<td>${fieldReportInstance.report.name}</td>

							</tr>
						</g:each>
						</tbody>
					</table>
					<div class="paginateButtons">
						<g:paginate total="${fieldReportInstanceTotal}" />
					</div>
				</div>
			</div>
		</div>

	</body>
</html>
