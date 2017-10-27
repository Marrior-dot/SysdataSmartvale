
<%@ page import="br.com.acception.greport.Report" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
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
				<div id="list-report" class="content scaffold-list" role="main">
					<g:if test="${flash.message}">
						<div class="message" role="status">${flash.message}</div>
					</g:if>
					<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
						<thead>
							<tr>
								<g:sortableColumn property="id" title="${message(code: 'report.id.label', default: 'ID')}" />
								<g:sortableColumn property="name" title="${message(code: 'report.name.label', default: 'Nome')}" />
							</tr>
						</thead>
						<tbody>
						<g:each in="${reportInstanceList}" var="reportInstance">
							<tr>
								<td>${fieldValue(bean: reportInstance, field: "id")}</td>
								<td><g:link action="show" id="${reportInstance.id}">${fieldValue(bean: reportInstance, field: "name")}</g:link></td>
							</tr>
						</g:each>
						</tbody>
					</table>
					<div class="pagination">
						<g:paginate total="${reportInstanceTotal}" />
					</div>
				</div>
			</div>
		</div>
	</body>
</html>