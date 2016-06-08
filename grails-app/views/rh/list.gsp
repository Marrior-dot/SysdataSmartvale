
<%@ page import="com.sysdata.gestaofrota.Rh" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'rh.label', default: 'RH')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
           	<g:form>
           	
	           	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
		            <div class="buttons">
			            <span class="button"><g:actionSubmit class="new" action="create" value="${message(code:'default.new.label', args:[entityName]) }"/></span>
		            </div>
				</sec:ifAnyGranted>
           	
	            <div class="list">
	            	<g:render template="search"/>
	            </div>
            </g:form>
        </div>
    </body>
</html>
