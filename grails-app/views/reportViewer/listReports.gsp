<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
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
			<a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
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
						<sec:ifAnyGranted roles="ROLE_LOG,ROLE_HELP">
							<g:if test="${reportInstance.name=="Transações Combustível por Período"}">
								<li><g:link action="openToParameters" id="${reportInstance.id}">${reportInstance.name}</g:link></li>
							</g:if>
							<g:elseif test="${reportInstance.name=="Detalhes Projecao Reembolso"}">
								<li></li>
							</g:elseif>
							<g:else></g:else>
						</sec:ifAnyGranted>
						<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_RH,ROLE_ESTAB">
							<g:if test="${reportInstance.name!="Detalhes Projecao Reembolso"}">
								<li><g:link action="openToParameters" id="${reportInstance.id}">${reportInstance.name}</g:link></li>
							</g:if>
						</sec:ifAnyGranted>
					</g:each>

				</ul>


			</div>
		</div>
	</div>
    </body>
</html>
