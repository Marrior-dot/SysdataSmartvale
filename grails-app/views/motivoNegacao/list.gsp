
<%@ page import="com.sysdata.gestaofrota.MotivoNegacao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'motivoNegacao.label', default: 'MotivoNegacao')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
	
		<div id="list-motivoNegacao" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="codigo" title="${message(code: 'motivoNegacao.codigo.label', default: 'Codigo')}" />
					
						<g:sortableColumn property="descricao" title="${message(code: 'motivoNegacao.descricao.label', default: 'Descricao')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${motivoNegacaoInstanceList}" status="i" var="motivoNegacaoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${motivoNegacaoInstance.id}">${fieldValue(bean: motivoNegacaoInstance, field: "codigo")}</g:link></td>
					
						<td>${fieldValue(bean: motivoNegacaoInstance, field: "descricao")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="paginateButtons">
				<g:paginate total="${motivoNegacaoInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
