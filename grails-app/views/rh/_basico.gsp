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
				<div class="col-md-4">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ" class="cnpj" required="true" value="${rhInstance?.cnpj}" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<bs:formField class="uppercase" id="nome" label="Razão Social" required="true" value="${rhInstance?.nome}" />
				</div>

				<div class="col-md-6">
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
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaAdministracao">Taxa Pedido *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="taxaPedido" id="taxaPedido" value="${rhInstance?.taxaPedido}"
							   min="0" max="100" step="0.01" required/>
						<span class="input-group-addon">%</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaAdministracao">Validade Carga *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="validadeCarga" id="validadeCarga" value="${rhInstance?.validadeCarga}"
							   min="0" required/>
						<span class="input-group-addon">dias</span>
					</div>
				</div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">Cartão</div>

        <div class="panel-body">
			<div class="form-group col-md-4">
				<label for="vinculoCartao">Vincular Cartão a:</label>
				<div class="input-group">
					<g:if test="${action == Util.ACTION_VIEW}">
						<input type="text" class="form-control" name="vinculoCartao" id="vinculoCartao" disabled value="${rhInstance?.vinculoCartao}"/>
					</g:if>
					<g:else>
						<g:select name="vinculoCartao" from="${com.sysdata.gestaofrota.TipoVinculoCartao.values()}"
								  disabled="${rhInstance?.portadoresCount > 0}"
								  class="form-control" optionKey="key" value="${rhInstance?.vinculoCartao}"/>
					</g:else>
					<span class="input-group-addon">
						<input type="checkbox" name="cartaoComChip" id="cartaoComChip" ${rhInstance.cartaoComChip ? 'checked' : ''}>
						<strong>Cartão com chip</strong>
					</span>
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
					<div class="input-group">
						<input id="diasToleranciaAtraso" name="diasToleranciaAtraso" type="number" class="form-control"
							   min="0" value="${rhInstance?.diasToleranciaAtraso}" required>
						<span class="input-group-addon">dias</span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="panel panel-default">
        <div class="panel-heading">
			Taxas do Cartão
		</div>

		<div class="panel-body">
            <div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaUtilizacao">Utilização *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control" name="taxaUtilizacao" id="taxaUtilizacao"
							   value="${rhInstance?.taxaUtilizacao}" min="0" step="0.01" required/>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaMensalidade">Mensalidade *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control" name="taxaMensalidade" id="taxaMensalidade"
							   value="${rhInstance?.taxaMensalidade}" min="0" step="0.01" required/>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaEmissaoCartao">Emissão *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control" name="taxaEmissaoCartao" id="taxaEmissaoCartao"
							   value="${rhInstance?.taxaEmissaoCartao}" min="0" step="0.01" required/>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaReemissaoCartao">Reemissão *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control" name="taxaReemissaoCartao" id="taxaReemissaoCartao"
							   value="${rhInstance?.taxaReemissaoCartao}" min="0" step="0.01" required/>
					</div>
				</div>
            </div>
		</div>
    </div>

	<div class="panel panel-default">
		<div class="panel-heading">
			Parâmetros do Programa
		</div>
		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaAdministracao">Taxa de Administração *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="taxaAdministracao" id="taxaAdministracao"
							   value="${rhInstance?.taxaAdministracao}" min="0" max="100" step="0.01" required/>
						<span class="input-group-addon">%</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaAdministracao">Taxa de Manutenção *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="taxaManutencao" id="taxaManutencao"
							   value="${rhInstance?.taxaManutencao}" min="0" max="100" step="0.01" required/>
						<span class="input-group-addon">%</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="multaAtraso">Multa por Atraso *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control" name="multaAtraso" id="multaAtraso"
							   value="${rhInstance?.multaAtraso}" min="0" step="0.01" required/>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="jurosProRata">Juros Pró-Rata por Atraso *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="jurosProRata" id="jurosProRata"
							   value="${rhInstance?.jurosProRata}" min="0" max="100" step="0.01" required/>
						<span class="input-group-addon">%</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="prazoPgtFatura">Prazo Pagamento Fatura *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="prazoPgtFatura" id="prazoPgtFatura"
							   value="${rhInstance?.prazoPgtFatura}" min="0" required/>
						<span class="input-group-addon">dias</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="modeloCobranca">Modelo Cobrança *</label>
					<div class="input-group">
						<g:select name="modeloCobranca" from="${com.sysdata.gestaofrota.TipoCobranca.values()}"
							class="form-control" optionKey="key" optionValue="nome" value="${rhInstance?.modeloCobranca}" required="required"/>
						<span class="input-group-addon">
							<input type="checkbox" id="renovarLimite" name="renovarLimite" ${rhInstance?.renovarLimite ? 'checked' : ''}>
							<strong>Renovar Limite</strong>
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="panel panel-default">
		<div class="panel-heading">
			Parâmetros do Autorizador
		</div>

		<div class="panel-body">
			<div class="form-group col-md-3">
				<label class="control-label" for="maximoTrnPorDia">Máximo Transações *</label>
				<div class="input-group">
					<input type="number" class="form-control" name="maximoTrnPorDia" id="maximoTrnPorDia" value="${rhInstance?.maximoTrnPorDia}"
						   min="0" required/>
					<span class="input-group-addon">por dia</span>
				</div>
			</div>

			<div class="form-group col-md-3">
				<label class="control-label" for="diasInatividade">Inatividade *</label>
				<div class="input-group">
					<input type="number" class="form-control" name="diasInatividade" id="diasInatividade" value="${rhInstance?.diasInatividade}"
						   min="0" required/>
					<span class="input-group-addon">dias</span>
				</div>
			</div>
		</div>
	</div>

	<g:if test="${action == Util.ACTION_VIEW}">
		<g:link class="btn btn-default" action="edit" id="${rhInstance.id}">
			<span class="glyphicon glyphicon-edit"></span>
			<g:message code="default.button.edit.label" default="Edit"/>
		</g:link>

		<g:actionSubmit class="btn btn-danger" action="delete" value="Inativar"/>
	</g:if>
	<g:elseif test="${action == Util.ACTION_NEW}">
		<g:actionSubmit class="btn btn-default" action="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/>
	</g:elseif>
	<g:elseif test="${action == Util.ACTION_EDIT}">
		<g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
	</g:elseif>
</g:form>