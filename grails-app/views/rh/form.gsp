<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'rh.label', default: 'RH')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${rhInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span> 
                <g:renderErrors bean="${rhInstance}" as="list" />
            </div>
            </g:hasErrors>
           	<g:if test="${action==Util.ACTION_VIEW}">
           		<gui:tabView>
        			<gui:tab label="${entityName}" active="true">
        				<g:render template="basico"/>
        			</gui:tab>
        			<gui:tab label="${message(code:'unidade.label')}">
        				<g:render template="/unidade/search" model="[controller:'unidade',rhId:rhInstance?.id]"/> 
        			</gui:tab>
        			<gui:tab label="Categorias de Funcionários">	
        				<g:render template="/categoriaFuncionario/search" model="[controller:'categoriaFuncionario',rhId:rhInstance?.id]"/>
        			</gui:tab>
        			
        			<gui:tab label="Estabelecimentos">
        				<g:render template="estabelecimentos" bean="${rhInstance}"/>
        			</gui:tab>
        			
        		</gui:tabView>
           	</g:if>
           	<g:else>
           		<g:render template="basico"/>
           	</g:else>
		</div>			
    </body>
</html>


    
