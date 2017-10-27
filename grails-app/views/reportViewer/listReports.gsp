<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="Relatórios" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
	<br><br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4><g:message code="default.list.label" args="[entityName]" /></h4>
		</div>
		<div class="panel-body">
			<a class="btn btn-default" href="${createLink(uri: '/')}">
				<span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/>
			</a>
			<br><br>
			<div class="body">
				<g:if test="${flash.message}">
					<div class="message">${flash.message}</div>
				</g:if>
				<g:if test="${flash.errors}">
					<div class="errors">
						<span style="font-weight:bold;padding-left:10px">ERROS</span>
						<ul>
							<g:each in="${flash.errors}" var="err">
								<li>${err}</li>
							</g:each>
						</ul>
					</div>
				</g:if>

				<ul>
					<g:each in="${reportList}" var="reportInstance">
						<sec:ifAnyGranted roles="${reportInstance?.roles?.authority?.join(', ')}">
							<li><g:link action="openToParameters" id="${reportInstance.id}">${reportInstance.name}</g:link></li>
						</sec:ifAnyGranted>
					</g:each>
				</ul>

			</div>
		</div>
	</div>
    </body>
</html>
