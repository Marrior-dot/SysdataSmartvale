<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
    <g:hiddenField name="id" value="${unidadeInstance?.id}" />
    <g:hiddenField name="version" value="${unidadeInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>
    <g:hiddenField name="rhId" value="${rhId}"/>
            
	<div class="enter">
	
		<fieldset class="uppercase">
			<h2>Dados Básicos</h2>
			
			<g:if test="${action==Util.ACTION_VIEW}">
				<div>
					<label><span>Código</span>${unidadeInstance?.id}</label>
					<div class="clear"></div>
				</div>
			</g:if>
			<div>
				<label><span>Nome</span><g:textField name="nome" value="${unidadeInstance?.nome}" size="50" maxlength="50"/></label>
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
