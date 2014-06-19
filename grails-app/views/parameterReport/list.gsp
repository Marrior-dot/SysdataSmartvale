
<%@ page import="br.com.acception.greport.ParameterReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'parameterReport.label', default: 'ParameterReport')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-parameterReport" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-parameterReport" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="dataType" title="${message(code: 'parameterReport.dataType.label', default: 'Data Type')}" />
					
						<g:sortableColumn property="label" title="${message(code: 'parameterReport.label.label', default: 'Label')}" />
					
						<g:sortableColumn property="markupType" title="${message(code: 'parameterReport.markupType.label', default: 'Markup Type')}" />
					
						<g:sortableColumn property="name" title="${message(code: 'parameterReport.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="order" title="${message(code: 'parameterReport.order.label', default: 'Order')}" />
					
						<th><g:message code="parameterReport.report.label" default="Report" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${parameterReportInstanceList}" status="i" var="parameterReportInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${parameterReportInstance.id}">${fieldValue(bean: parameterReportInstance, field: "id")}</g:link></td>
					
						<td>${fieldValue(bean: parameterReportInstance, field: "label")}</td>
					
						<td>${fieldValue(bean: parameterReportInstance, field: "markupType")}</td>
					
						<td>${fieldValue(bean: parameterReportInstance, field: "name")}</td>
					
						<td>${fieldValue(bean: parameterReportInstance, field: "order")}</td>
					
						<td>${parameterReportInstance.report.name}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${parameterReportInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
