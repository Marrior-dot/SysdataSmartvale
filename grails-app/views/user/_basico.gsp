<%@page import="com.sysdata.gestaofrota.Processadora"%>
<%@page import="com.sysdata.gestaofrota.Administradora"%>
<%@page import="com.sysdata.gestaofrota.Rh"%>
<%@page import="com.sysdata.gestaofrota.PostoCombustivel"%>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<script type="text/javascript" src="${resource(dir:'js',file:'roles.js') }"></script>

<g:form method="post">

	<g:hiddenField name="id" value="${userInstance?.id}"/>
	
	<fieldset>
		<h2>Dados de Usuário</h2>
		
<%
		
		def l=[:]
		l=ownerList.collect{
					if(it instanceof PostoCombustivel){
						[id:it.id,
						nome:"Estabelecimento - " + it.nome]
					}else if(it instanceof Rh){
						[id:it.id,
						nome:"RH - " + it.nome]
					}else if(it instanceof Administradora){
						[id:it.id,
						nome:"Administradora - " + it.nome]
					}else if(it instanceof Processadora){
						[id:it.id,
						nome:"Processadora - " + it.nome]
					}
				}
		 %>		
		
		<div><label><span>Organização</span><g:select id="owner" name="owner.id" from="${l}" value="${userInstance?.owner?.id}" optionKey="id" optionValue="nome"></g:select></label></div>
		
		
		
		<g:if test="${action in [Util.ACTION_NEW]}">
			<div><label><span>Papel</span><g:select name="role" from="" value="" optionKey="id" optionValue="authority"></g:select></label></div>
		</g:if>
		
		
		
		<g:if test="${action in [Util.ACTION_EDIT]}">
			<div><label><span>Papel : ${role?.authority}</span><g:select name="role" from="" value="" optionKey="id" optionValue="authority"></g:select></label></div>
		</g:if>
		
		
		
		<g:if test="${action in [Util.ACTION_VIEW]}">
			<div><label><span>Papel : ${role?.authority}</span></label></div>
		</g:if>
		
		
		
		
		<div><label><span>Nome</span><g:textField name="name" value="${userInstance?.name}" /></label></div>
		
		<div><label><span>Email</span><g:textField name="email" value="${userInstance?.email}" /></label></div>
		
		<g:if test="${action in [Util.ACTION_NEW]}">
			<div><label><span>Login</span><g:textField name="username" value="${userInstance?.username}" /></label></div>
			
			<div><label><span>Senha</span><g:passwordField name="password" value="${userInstance?.password}" /></label></div>
			
			<div><label><span>Confirme a Senha</span><g:passwordField name="confirmPassword" value="" /></label></div>
		
		</g:if>
		
		<div><label><span>Habilitado</span><g:checkBox name="enabled" value="${userInstance?.enabled}" /></label></div>
		
	</fieldset>
	
	
	<div class="buttons">
		<g:if test="${action in [Util.ACTION_NEW]}">
			<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
		</g:if>
		<g:if test="${action in [Util.ACTION_VIEW]}">
        	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        	<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
        </g:if>
        <g:if test="${action in [Util.ACTION_EDIT]}">
        	<span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        	<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
        </g:if>
	</div>
	
</g:form>
