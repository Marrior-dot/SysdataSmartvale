
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
					<li><g:link action="openToParameters" id="${reportInstance.id}">${reportInstance.name}</g:link>  </li>
				</g:each>
			
			</ul>
			
			            
        </div>
    </body>
</html>
