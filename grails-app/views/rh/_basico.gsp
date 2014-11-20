<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
    <g:hiddenField name="id" value="${rhInstance?.id}" />
    <g:hiddenField name="version" value="${rhInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>


	<div class="enter">
		<fieldset class="uppercase">
			<h2>Dados Básicos</h2>
			<div style="padding:0.5em;font-size:16px;">
				<g:if test="${action==Util.ACTION_VIEW}">
					<span>Código: ${rhInstance?.codigo}</span>
				</g:if>
			</div>
			<div>
				<label><span>CNPJ</span><g:textField name="cnpj" value="${rhInstance?.cnpj}" /></label>
			</div>
			
			<div>
				<label><span>Razão Social</span><g:textField name="nome" value="${rhInstance?.nome}" size="50" maxlength="50" /></label>
				<div class="clear"></div>
			</div>
			
			<div>
				<label><span>Nome Fantasia</span><g:textField name="nomeFantasia" value="${rhInstance?.nomeFantasia}" size="50" maxlength="50" /></label>
				<div class="clear"></div>
			</div>	
			
			<div>
				<label><span>Taxa de Pedido (%)</span><g:textField name="taxaPedido" value="${rhInstance?.taxaPedido}" /></label>
			</div>
			
			<div>
				<label><span>Validade da carga (dias)</span><g:textField name="validadeCarga" value="${rhInstance?.validadeCarga}" /></label>
			</div>
				
			
			<g:render template="/endereco/form" model="[enderecoInstance:rhInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>
			
			<g:render template="/telefone/form" model="[telefoneInstance:rhInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>
		</fieldset>					
	
	</div>
	
	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
		<div class="buttons">
			<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
				<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
			</g:if>
			<g:if test="${action==Util.ACTION_VIEW}">
				<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
				<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:if>
		</div>
	</sec:ifAnyGranted>
	
	
	


</g:form>