
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="Relatórios" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
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
    </body>
</html>
