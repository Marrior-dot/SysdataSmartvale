<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
    <g:hiddenField name="id" value="${rhInstance?.id}" />
    <g:hiddenField name="version" value="${rhInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>


	<div class="panel panel-default">

		<div class="panel-heading">
			Dados Básicos
		</div>

		<div class="panel-body">

			<div class="row">
				<div class="col-xs-4">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ" class="cnpj" required="true" value="${rhInstance?.cnpj}" />
				</div>

				<div class="col-xs-4">
					<bs:formField class="uppercase" id="nome" label="Razão Social" required="true" value="${rhInstance?.nome}" />
				</div>
			</div>

			<div class="row">
				<div class="col-xs-6">
					<bs:formField class="uppercase" id="nomeFantasia" label="Nome Fantasia" required="true" value="${rhInstance?.nomeFantasia}" />
				</div>
			</div>
		</div>

	</div>

	<g:render template="/endereco/form" model="[enderecoInstance: rhInstance?.endereco, endereco:'endereco', legend:'Endereço']"/>

	<g:render template="/telefone/form" model="[telefoneInstance: rhInstance?.telefone,telefone:'telefone', legend:'Telefone']"/>

    <div class="panel panel-default">
        <div class="panel-heading">
            Pedido de Carga
        </div>

        <div class="panel-body">

            <div class="row">
                <div class="col-xs-4">
                    <bs:formField required="true" id="taxaPedido" label="Taxa Pedido (%)" value="${rhInstance?.taxaPedido}" />
                </div>

                <div class="col-xs-4">
                    <bs:formField required="true" id="validadeCarga" label="Validade Carga (dias)" value="${rhInstance?.validadeCarga}" />
                </div>
            </div>
        </div>
    </div>


    <div class="panel panel-default">
        <div class="panel-heading">Cartão</div>

        <div class="panel-body">
			<div class="form-group col-md-3">
				<label for="vinculoCartao">Vincular Cartão a:</label>
				<g:if test="${action == Util.ACTION_VIEW}">
					<input type="text" class="form-control" name="vinculoCartao" id="vinculoCartao" disabled value="${rhInstance?.vinculoCartao}"/>
				</g:if>
				<g:else>
					<g:select name="vinculoCartao" from="${com.sysdata.gestaofrota.TipoVinculoCartao.values()}" disabled="${rhInstance?.portadoresCount > 0}"
							  class="form-control" optionKey="key" value="${rhInstance?.vinculoCartao}"/>
				</g:else>
			</div>

			<div class="form-group col-md-3">
				<div class="checkbox">
					<label>
						<input type="checkbox" name="cartaoComChip" id="cartaoComChip" ${rhInstance.cartaoComChip ? 'checked' : ''}> <strong>Cartão Com Chip</strong>
					</label>
				</div>
			</div>
        </div>
    </div>

	<div class="panel panel-default">
        <div class="panel-heading">Fatura</div>
		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="diasToleranciaAtraso">Dias de Tolerância a Atraso *</label>
					<input id="diasToleranciaAtraso" name="diasToleranciaAtraso" type="number" class="form-control" min="0" value="${rhInstance?.diasToleranciaAtraso}" required>
				</div>
			</div>
		</div>
	</div>


	<div class="panel panel-default">
        <div class="panel-heading">
			Taxas de Cartão
		</div>

		<div class="panel-body">
            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="taxaUtilizacao" name="taxaUtilizacao" label="Utilização (R\$)" value="${rhInstance?.taxaUtilizacao}" />
                </div>

                <div class="col-xs-4">
                    <bs:formField id="taxaMensalidade" label="Mensalidade (R\$)" value="${rhInstance?.taxaMensalidade}" />
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="taxaEmissaoCartao" name="taxaEmissaoCartao" label="Emissão de Cartão (R\$)" value="${rhInstance?.taxaEmissaoCartao}" />
                </div>

                <div class="col-xs-4">
                    <bs:formField id="taxaReemissaoCartao" label="Remissão de Cartão (R\$)" value="${rhInstance?.taxaReemissaoCartao}" />
                </div>
            </div>
		</div>
    </div>

	<div class="panel panel-default">
		<div class="panel-heading">
			Parâmetros Autorizador
		</div>

		<div class="panel-body">

			<div class="row">
				<div class="col-xs-3">
					<bs:formField required="true" id="maximoTrnPorDia" label="Máximo Transações Por Dia" value="${rhInstance?.maximoTrnPorDia}" />
				</div>

				<div class="col-xs-3">
					<bs:formField required="true" id="diasInatividade" label="Dias de Inatividade" value="${rhInstance?.diasInatividade}" />
				</div>
			</div>
		</div>
	</div>


	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
		<div class="buttons">
			<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
				<span class="button">
					<g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}"
									value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>

			</g:if>
			<g:if test="${action==Util.ACTION_VIEW}">
				<g:link class="btn btn-default" action="edit" id="${rhInstance.id}">
					<span class="glyphicon glyphicon-edit"></span>
					<g:message code="default.button.edit.label" default="Edit"/>
				</g:link>

				<g:form action="delete" id="${rhInstance.id}" method="delete">

					<button type="submit" class="btn btn-default"
							onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
						<span class="glyphicon glyphicon-floppy-remove"></span>
						<g:message code="default.button.delete.label" default="Inativar"/>
					</button>
				</g:form>
			</g:if>
		</div>
	</sec:ifAnyGranted>
</g:form>