<%@ page import="com.sysdata.gestaofrota.Unidade" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unidade.label', default: 'Unidade')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" controller="rh" action="show" params="[id:rhInstance?.id]"><g:message code="rh.label"/></g:link></span>
            <span class="menuButton"><g:link class="file" action="generateCartaSenha" params="[id:unidadeInstance?.id]">Gerar Carta Senha</g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${unidadeInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span> 
                <g:renderErrors bean="${unidadeInstance}" as="list" />
            </div>
            </g:hasErrors>
            
           	<fieldset style="border:1px solid;font-size:14px;">
				<label><span>${message(code: 'rh.label', default: 'RH')}</span>${rhInstance?.nome}</label>
				<div class="clear"></div>
	        </fieldset>
            
            
           	<g:if test="${action==Util.ACTION_VIEW}">
           		<gui:tabView>
        			<gui:tab label="${entityName}" active="true">
        				<g:render template="basico" model="[rhId:rhInstance?.id]"/>
        			</gui:tab>
        			<gui:tab label="Funcionários">	
        				<g:render template="/funcionario/search" model="[controller:'funcionario',unidade_id:unidadeInstance?.id]"/>
        			</gui:tab>
        			<gui:tab label="Veículos">
        				<g:render template="/veiculo/search" model="[controller:'veiculo',unidade_id:unidadeInstance?.id]"/> 
        			</gui:tab>
        			<gui:tab label="Equipamentos">
        				<g:render template="/equipamento/search" model="[controller:'equipamento',unidade_id:unidadeInstance?.id]"/> 
        			</gui:tab>
        		</gui:tabView>
           	</g:if>
           	<g:else>
           		<g:render template="basico" model="[rhId:rhInstance?.id]"/>
           	</g:else>
		</div>			
    </body>
</html>


    
