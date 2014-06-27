<%@ page import="com.sysdata.gestaofrota.PostoCombustivel" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'empresa.label', default: 'Empresa')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
            	<span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </sec:ifAnyGranted>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${postoCombustivelInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar Empresa</span> 
                <g:renderErrors bean="${postoCombustivelInstance}" as="list" />
            </div>
            </g:hasErrors>

				<g:if test="${action==Util.ACTION_VIEW}">
	             	<gui:tabView>
	             		<gui:tab label="Empresa" active="true">
	             			<g:render template="basico"/>
	             		</gui:tab>
	             		<gui:tab label="Calendário Reembolso">
	             			<g:render template="listReembolso"/>
	             		</gui:tab>
	             		<gui:tab label="Estabelecimentos">
	             			<g:render template="/estabelecimento/search" model="[controller:'estabelecimento',empId:postoCombustivelInstance?.id]" />
	             		</gui:tab>
	             		
	             	</gui:tabView>
				</g:if>
				<g:else>
					<g:render template="basico"/>
				</g:else>

        </div>
    </body>
</html>
