<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post">

	<g:hiddenField name="id" value="${userInstance?.id}"/>
	
	<fieldset>
		<h2>Dados de Usuário</h2>
		
		<div><label><span>Organização</span><g:select name="owner.id" from="${ownerList}" value="${userInstance?.owner?.id}" optionKey="id" optionValue="nome"></g:select></label></div>
		
		<div><label><span>Nome</span><g:textField name="name" value="${userInstance?.name}" /></label></div>
		
		<div><label><span>Login</span><g:textField name="username" value="${userInstance?.username}" /></label></div>
		
		<div><label><span>Senha</span><g:passwordField name="password" value="${userInstance?.password}" /></label></div>
		
		<div><label><span>Email</span><g:textField name="email" value="${userInstance?.email}" /></label></div>
		
		<g:if test="${action in [Util.ACTION_NEW]}">
			<div><label><span>Confirme a Senha</span><g:passwordField name="confirmPassword" value="" /></label></div>
		</g:if>
		
		<div><label><span>Habilitado</span><g:checkBox name="enabled" value="${userInstance?.enabled}" /></label></div>
		
	</fieldset>
	
	
	<div class="buttons">
		<g:if test="${action in [Util.ACTION_NEW]}">
			<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
		</g:if>
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	</div>
	
</g:form>