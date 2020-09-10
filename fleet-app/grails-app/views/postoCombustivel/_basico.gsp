<%@ page import="com.sysdata.gestaofrota.Util" %>

<br/>
<g:form method="post">
	<g:hiddenField name="id" value="${postoCombustivelInstance?.id}" />
	<g:hiddenField name="version" value="${postoCombustivelInstance?.version}" />
	<g:hiddenField name="action" value="${action}"/>

	<div class="panel panel-default panel-top">
		<div class="panel-heading">
			Dados Básicos
		</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-xs-6">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ"  value="${postoCombustivelInstance?.cnpj}" class="cnpj"/>
				</div>
				<div class="col-xs-6">
					<bs:formField id="nome" name="nome" label="Razão Social" value="${postoCombustivelInstance?.nome}"/>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12">
					<bs:formField id="nomeFantasia" name="nomeFantasia" label="Nome Fantasia" value="${postoCombustivelInstance?.nomeFantasia}"/>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-6">
					<bs:formField id="inscricaoEstadual" name="inscricaoEstadual" label="Inscrição Estadual" value="${postoCombustivelInstance?.inscricaoEstadual}" class="only-numbers" maxlength="10"/>
				</div>
				<div class="col-xs-6">
					<bs:formField id="inscricaoMunicipal" name="inscricaoMunicipal" label="Inscrição Municipal" value="${postoCombustivelInstance?.inscricaoMunicipal}" class="only-numbers" maxlength="10"/>
				</div>
			</div>
		</div>
	</div>

	<g:render template="/endereco/form" model="[enderecoInstance:postoCombustivelInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>

	<g:render template="/telefone/form" model="[telefoneInstance:postoCombustivelInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>

	<g:render template="/dadoBancario/form" model="[dadoBancarioInstance:postoCombustivelInstance?.dadoBancario,dadoBancario:'dadoBancario',legend:'Conta para Reembolso']"/>

	<div class="panel panel-default">
		<div class="panel-heading">
			Dados de Reembolso
		</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-md-4">
					<bs:formField id="taxaReembolso" required="true" name="taxaReembolso" label="Taxa"
								  value="${Util.formatCurrency(postoCombustivelInstance?.taxaReembolso)}" class="form-control percentual"/>
				</div>
			</div>
		</div>
	</div>

	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
		<div class="buttons">
			<g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
				<button type="submit" class="btn btn-success" name="_action_${action==Util.ACTION_NEW?'save':'update'}">
					<span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.update.label" default="Update"></g:message>
				</button>
			</g:if>
			<g:if test="${action==Util.ACTION_VIEW}">

				<button type="submit" class="btn btn-default" name="_action_edit">
					<span class="glyphicon glyphicon-edit"></span>&nbsp;<g:message code="default.button.edit.label" default="Edit"></g:message>
				</button>

				<button type="submit" class="btn btn-danger" name="_action_delete"
						onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
					<span class="glyphicon glyphicon-remove"></span>&nbsp;<g:message code="default.button.delete.label" default="Delete"></g:message>
				</button>

			</g:if>
		</div>
	</sec:ifAnyGranted>
</g:form>


