
<%@ page import="com.sysdata.gestaofrota.Role" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap-layout">
		<g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<br><br>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.list.label" args="[entityName]" /></h4>
			</div>
			<div class="panel-body">
				<div class="nav">
					<ul>
						<a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
						<g:link class="btn btn-default" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>
					</ul>
				</div>
				<div id="list-role" class="content scaffold-list" role="main">
					<g:if test="${flash.message}">
						<div class="message" role="status">${flash.message}</div>
					</g:if>
					<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
						<thead>
						<tr>

							<g:sortableColumn property="authority" title="${message(code: 'role.authority.label', default: 'Authority')}" />

							<th><g:message code="role.owner.label" default="Owner" /></th>

						</tr>
						</thead>
						<tbody>
						<g:each in="${roleInstanceList}" status="i" var="roleInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

								<td><g:link action="show" id="${roleInstance.id}">${fieldValue(bean: roleInstance, field: "authority")}</g:link></td>

								<td>${fieldValue(bean: roleInstance, field: "owner")}</td>

							</tr>
						</g:each>
						</tbody>
					</table>
					<div class="pagination">
						<g:paginate total="${roleInstanceTotal}" />
					</div>
				</div>
			</div>
		</div>

	</body>
</html>
