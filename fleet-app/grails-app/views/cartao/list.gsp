
<%@ page import="com.sysdata.gestaofrota.Cartao" %>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="layout-restrito"/>
		<g:set var="entityName" value="Cartão"/>
		<title>Lista de Cartões</title>
	</head>
	<body>
		<div class="panel panel-default panel-top">
			<div class="panel-heading">
				<h4><g:message code="default.list.label" args="[entityName]"/></h4>
			</div>

			<div class="panel-body">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="alert">${flash.message}</div>
				</g:if>
				<a class="btn btn-default" href="${createLink(uri: '/')}"><span
						class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>

				<div class="panel-top">
					<g:render template="search" model="[controller: 'cartao']"/>
				</div>

			</div>
		</div>
	</body>
</html>
