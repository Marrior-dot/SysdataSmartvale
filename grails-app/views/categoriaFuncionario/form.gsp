<%@ page import="com.sysdata.gestaofrota.Unidade" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'categoriaFuncionario.label', default: 'Categoria de Funcionário')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" controller="rh" action="show" params="[id:rhInstance?.id]"><g:message code="rh.label"/></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${categoriaFuncionarioInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span> 
                <g:renderErrors bean="${categoriaFuncionarioInstance}" as="list" />
            </div>
            </g:hasErrors>
            
           	<fieldset style="border:1px solid;font-size:14px;">
				<label><span>RH</span>${rhInstance?.nome}</label>
				<div class="clear"></div>
	        </fieldset>
            
            
			<g:form method="post" >
			    <g:hiddenField name="id" value="${categoriaFuncionarioInstance?.id}" />
			    <g:hiddenField name="version" value="${categoriaFuncionarioInstance?.version}" />
			    <g:hiddenField name="action" value="${action}"/>
			    <g:hiddenField name="rhId" value="${rhInstance?.id}"/>
			            
				<div class="enter">
				
					<fieldset class="uppercase">
						<h2>Dados Básicos</h2>
						<g:if test="${action==Util.ACTION_VIEW}">
							<div>
								<label><span>Código</span>${categoriaFuncionarioInstance?.id}</label>
								<div class="clear"></div>
							</div>
						</g:if>
						
						<div>
							<label><span>Nome</span><g:textField name="nome" value="${categoriaFuncionarioInstance?.nome}" size="50" maxlength="50"/></label>
							<div class="clear"></div>
						</div>

						<div>
							<label><span>Valor Carga</span><g:textField class="currency" name="valorCarga" value="${Util.formatCurrency(categoriaFuncionarioInstance?.valorCarga)}" /></label>
							<div class="clear"></div>
						</div>
						
					</fieldset>					
				
				</div>
				<div class="buttons">
					<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
						<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
					</g:if>
					<g:if test="${action==Util.ACTION_VIEW}">
						<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
						<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
					</g:if>
				</div>
			            
			</g:form>
		</div>			
    </body>
</html>


    
