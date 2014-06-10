<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionario')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" controller="unidade" action="show" id="${unidadeInstance?.id}"><g:message code="unidade.label"/></g:link></span>
            <span class="menuButton"><g:link class="create" action="create" params="[unidade_id:unidadeInstance?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${funcionarioInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar Funcionário</span> 
                <g:renderErrors bean="${funcionarioInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:if test="${flash.errors}">
	            <div class="errors">
	                <ul>
	                	<g:each in="${flash.errors}" var="err">
	                		<li>${err}</li>
	                	</g:each>
	                </ul>
	            </div>
			</g:if>            
           	<g:if test="${action==Util.ACTION_VIEW}">
           		<gui:tabView>
        			<gui:tab label="${entityName}" active="true">
        				<g:render template="basico"/>
        			</gui:tab>
        			<gui:tab label="Cartões">
        				<g:render template="cartao"/>
        			</gui:tab>
        		</gui:tabView>
           	</g:if>
           	<g:else>
           		<g:render template="basico"/>
           	</g:else>
        </div>
    </body>
</html>
