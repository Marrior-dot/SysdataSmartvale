<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
    <g:hiddenField name="id" value="${rhInstance?.id}" />
    <g:hiddenField name="version" value="${rhInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>


	<div class="panel panel-default">

		<div class="panel-heading">Dados Básicos</div>


		<div style="padding:0.5em;font-size:16px;">
			<g:if test="${action==Util.ACTION_VIEW}">
				<span>Código: ${rhInstance?.codigo}</span>
			</g:if>
		</div>


		<div class="panel-body">

			<div class="row">

				<div class="col-xs-4">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ" class="cnpj" value="${rhInstance?.cnpj}" />
				</div>

				<div class="col-xs-4">
					<bs:formField class="uppercase" id="razaoSocial" label="Razão Social" required="true" value="${rhInstance?.nome}" />
				</div>

			</div>

			<div class="row">
				<div class="col-xs-6">
					<bs:formField class="uppercase" id="nomeFantasia" label="Nome Fantasia" required="true" value="${rhInstance?.nomeFantasia}" />
				</div>
			</div>

			<div class="row">
				<div class="col-xs-4">
					<bs:formField class="uppercase" id="taxaPedido" label="Taxa Pedido (%)" value="${rhInstance?.taxaPedido}" />
				</div>

				<div class="col-xs-4">
					<bs:formField class="uppercase" id="validadeCarga" label="Validade Carga (dias)" value="${rhInstance?.validadeCarga}" />
				</div>

			</div>
		</div>


		<g:render template="/endereco/form" model="[enderecoInstance:rhInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>
		<g:render template="/telefone/form" model="[telefoneInstance:rhInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>


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