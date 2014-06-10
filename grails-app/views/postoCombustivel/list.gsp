
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'empresa.label', default: 'Empresa')}" />
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
           	<g:form>
	            <div class="buttons">
		            <span class="button"><g:actionSubmit class="new" action="create" value="${message(code:'default.new.label', args:[entityName]) }"/></span>
	            </div>
	            <div class="list">
	            	<g:render template="search"/>
	            </div>
            </g:form>
        </div>
    </body>
</html>
