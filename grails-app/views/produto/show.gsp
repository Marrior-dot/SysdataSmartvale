<%@ page import="com.sysdata.gestaofrota.Produto" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="layout-restrito">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<br/>
		<div class="body">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4><g:message code="default.show.label" args="[entityName]" /></h4>
			</div>
			<div class="panel-body">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="alert"><strong>${flash.message}</strong></div>
				</g:if>

				<div class="buttons">
					<a type="button" class="btn btn-default" class="home" href="${createLink(uri: '/')}">
						<i class="glyphicon glyphicon-home"></i>
						<g:message code="default.home.label"/>
					</a>
					<g:link type="button" class="btn btn-default" action="list">
						<i class="glyphicon glyphicon-th-list"></i>
						<g:message code="default.list.label" args="[entityName]"/>
					</g:link>
					<g:link type="button" class="btn btn-default" action="create">
						<i class="glyphicon glyphicon-plus"></i>
						<g:message code="default.new.label" args="[entityName]" />
					</g:link>
				</div>
				<br/>


				<g:render template="form"/>

				<g:form>
					<g:hiddenField name="action" value="visualizando" />
					<g:hiddenField name="id" value="${produtoInstance?.id}" />
					<g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
					<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
									onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
				</g:form>

			</div>
		</div>
	</div>
	</body>
</html>
