<%@ page import="com.sysdata.gestaofrota.Estabelecimento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'estabelecimento.label', default: 'Estabelecimento')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	            <span class="menuButton"><g:link class="list" controller="postoCombustivel" action="show" id="${empresaInstance?.id}"><g:message code="empresa.label"/></g:link></span>
	        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
	            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
	        </sec:ifAnyGranted>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${estabelecimentoInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar Estabelecimento</span> 
                <g:renderErrors bean="${estabelecimentoInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${estabelecimentoInstance?.id}" />
                <g:hiddenField name="version" value="${estabelecimentoInstance?.version}" />
                <g:hiddenField name="empId" value="${empresaInstance?.id}" />
                <g:hiddenField name="action" value="${action}"/>
                	
                <g:render template="basico"/>	
                
				<div class="buttons">
					<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
						<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
					</g:if>
					<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
						<g:if test="${action==Util.ACTION_VIEW}">
							<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
							<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
						</g:if>
					</sec:ifAnyGranted>
				</div>
                	
            </g:form>
        </div>
    </body>
</html>
