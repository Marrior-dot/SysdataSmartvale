<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
	<g:hiddenField name="id" value="${postoCombustivelInstance?.id}" />
	<g:hiddenField name="version" value="${postoCombustivelInstance?.version}" />
	<g:hiddenField name="action" value="${action}"/>


<div class="dialog">
	<div class="enter">
		<fieldset class="uppercase">
			<h2>Dados Básicos</h2>
			<label><span>CNPJ</span><g:textField name="cnpj" value="${postoCombustivelInstance?.cnpj}" /></label>
			<label><span>Razão Social</span><g:textField name="nome" value="${postoCombustivelInstance?.nome}" size="50" maxlength="50"/></label>
			<div>
				<label><span>Nome Fantasia</span><g:textField name="nomeFantasia" value="${postoCombustivelInstance?.nomeFantasia}" size="50" maxlength="50"/></label>
				<div class="clear"></div>
			</div>
			<label><span>Inscrição Estadual</span><g:textField name="inscricaoEstadual" value="${postoCombustivelInstance?.inscricaoEstadual}" class="numeric" size="10" maxlength="10"/></label>
			<label><span>Inscrição Municipal</span><g:textField name="inscricaoMunicipal" value="${postoCombustivelInstance?.inscricaoMunicipal}" class="numeric" size="10" maxlength="10"/></label>
		</fieldset>
		
		<g:render template="/endereco/form" model="[enderecoInstance:postoCombustivelInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>
		
		<g:render template="/telefone/form" model="[telefoneInstance:postoCombustivelInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>
		
		<g:render template="/dadoBancario/form" model="[dadoBancarioInstance:postoCombustivelInstance?.dadoBancario,dadoBancario:'dadoBancario',legend:'Conta para Reembolso']"/>
		
		<fieldset>
			<h2>Dados de Reembolso</h2>
			<label><span>Taxa</span><g:textField name="taxaReembolso" class="currency" value="${Util.formatCurrency(postoCombustivelInstance?.taxaReembolso)}" /></label>
		</fieldset>
		
		
	</div>
		
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