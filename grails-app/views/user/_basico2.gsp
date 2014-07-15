<%@page import="com.sysdata.gestaofrota.Util" %>
<%@page import="com.sysdata.gestaofrota.Administradora"%>
<%@page import="com.sysdata.gestaofrota.Rh"%>
<%@page import="com.sysdata.gestaofrota.PostoCombustivel"%>
<%@page import="com.sysdata.gestaofrota.Processadora"%>


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
		
		<div><label><span>Organização</span><g:select name="owner.id" from="${l}" value="${userInstance?.owner?.id}" optionKey="id" optionValue="nome"></g:select></label></div>
		
		<div><label><span>Nome</span><g:textField name="name" value="${userInstance?.name}" /></label></div>
		
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
		<g:elseif test="${action in [Util.ACTION_VIEW] && !userInstance?.enabled}">
			<span class="button"><g:actionSubmit class="unlock" action="enableUser" value="Habilitar"/></span>
		</g:elseif>
		<g:elseif test="${action in [Util.ACTION_VIEW] && userInstance?.enabled}">
			<span class="button"><g:actionSubmit class="lock" action="unableUser" value="Desabilitar"/></span>
		</g:elseif>
		
	</div>
	
</g:form>