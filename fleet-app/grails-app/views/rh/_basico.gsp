<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Util" %>
<script type="application/javascript">
	$(document).ready(function () {
		alterarModeloCobranca();
	});

	function alterarModeloCobranca() {
		var duration = 500;
		/*var modeloCobranca = $("select#modeloCobranca").val();*/
		var modeloCobranca = "PRE_PAGO"
		var pedidoCargaPanel = $("div.panel#pedido-carga");
		var faturaPanel = $("div.panel#fatura");

		if (modeloCobranca === "POS_PAGO") {
			pedidoCargaPanel.find("input").each(function (index, element) {
				element.value = '0';
			});
			pedidoCargaPanel.hide(duration);
			faturaPanel.show(duration);
		}
		else if (modeloCobranca === "PRE_PAGO") {
			pedidoCargaPanel.show(duration);
			faturaPanel.find("input").each(function (index, element) {
				element.value = '0';
			});
			faturaPanel.hide(duration);
		}
	}
</script>

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
				<div class="col-md-6">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ" class="cnpj" required="true" value="${rhInstance?.cnpj}" />
				</div>

				<g:set var="bloquearModCob" value="${action == Util.ACTION_EDIT && (rhInstance?.funcionariosCount > 0 || rhInstance?.veiculosCount > 0)}"/>
%{--
				<div class="form-group col-md-6 ${bloquearModCob ? 'has-warning' : ''}">
					<label class="control-label" for="modeloCobranca">Modelo Cobrança *</label>
					<g:select name="modeloCobranca" from="${com.sysdata.gestaofrota.TipoCobranca.list()}" class="form-control"
							  optionValue="nome" value="${rhInstance?.modeloCobranca}" onchange="alterarModeloCobranca()"
							  aria-describedby="modelo-cobranca" disabled="${bloquearModCob}" required="required"/>
				<g:if test="${bloquearModCob}">
					<span id="modelo-cobranca" class="help-block">Não é possível alterar o Modelo Cobrança. Você já possui funcionários/veiculos cadastrados.</span>
				</g:if>
				</div>
--}%
			</div>
			<div class="row">
				<div class="col-md-6">
					<bs:formField class="form-control" id="nome" label="Razão Social" required="true" value="${rhInstance?.nome}" />
				</div>

				<div class="col-md-6">
					<bs:formField class="form-control" id="nomeFantasia" label="Nome Fantasia" required="true" value="${rhInstance?.nomeFantasia}" />
				</div>
			</div>
		</div>
	</div>

%{--	<div class="panel panel-default">
		<div class="panel-heading">Cartão</div>

		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-6 ${bloquearModCob ? 'has-warning' : ''}">
					<label class="control-label" for="vinculoCartao">Vincular Cartão a:</label>

					<div class="input-group">
						<g:if test="${action == Util.ACTION_VIEW}">
							<input type="text" class="form-control" name="vinculoCartao" id="vinculoCartao" disabled
								   value="${rhInstance?.vinculoCartao}" aria-describedby="vinculo-cartao"/>
						</g:if>
						<g:else>
							<g:select name="vinculoCartao" from="${com.sysdata.gestaofrota.TipoVinculoCartao.values()}"
									  disabled="${bloquearModCob}" class="form-control" aria-describedby="vinculo-cartao"
									  optionKey="key" value="${rhInstance?.vinculoCartao}"/>
						</g:else>
						<span class="input-group-addon">
							<input type="checkbox" name="cartaoComChip" id="cartaoComChip" ${rhInstance.cartaoComChip || action == Util.ACTION_NEW ? 'checked' : ''} ${bloquearModCob ? 'disabled': ''}>
							<strong>Cartão com chip</strong>
						</span>
						--}%%{--
                                                <span class="input-group-addon">
                                                    <input type="checkbox" id="renovarLimite" name="renovarLimite" ${rhInstance?.renovarLimite ? 'checked' : ''} ${bloquearModCob ? 'disabled': ''}>
                                                    <strong>Renovar Limite</strong>
                                                </span>
                        --}%%{--
					</div>
					<g:if test="${bloquearModCob}">
						<span id="vinculo-cartao" class="help-block">Não é possível alterar o Vinculo Cartão. Você já possui funcionários/veiculos cadastrados.</span>
					</g:if>
				</div>
			</div>
		</div>
	</div>--}%

	<div class="panel panel-default" id="pedido-carga">
		<div class="panel-heading">
			Pedido de Carga
		</div>

		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<div class="input-group">
						<bs:formField name="taxaPedido" label="Taxa Pedido" class="form-control percentual" value="${rhInstance?.taxaPedido}"
									  required="true"></bs:formField>
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

	<div class="panel panel-default" id="fatura">
		<div class="panel-heading">Fatura</div>
		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="diasToleranciaAtraso">Dias de Tolerância a Atraso *</label>
					<div class="input-group">
						<input id="diasToleranciaAtraso" name="diasToleranciaAtraso" type="number" action == Util.ACTION_VIEWclass="form-control"
							   min="0" value="${rhInstance?.diasToleranciaAtraso}" required>
						<span class="input-group-addon">dias</span>
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
							   value="${rhInstance?.jurosProRata}" min="0" step="0.01" max="100.00" required/>
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
			</div>
		</div>
	</div>

	<g:render template="/endereco/form" model="[enderecoInstance: rhInstance?.endereco, endereco:'endereco', legend:'Endereço']"/>

	<g:render template="/telefone/form" model="[telefoneInstance: rhInstance?.telefone,telefone:'telefone', legend:'Telefone']"/>

	<g:if test="${rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO}">
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

					<input type="hidden" name="taxaMensalidade" id="taxaMensalidade" value="0">
					<input type="hidden" name="taxaEmissaoCartao" id="taxaEmissaoCartao" value="0">
					<input type="hidden" name="taxaReemissaoCartao" id="taxaReemissaoCartao" value="0">
				</div>
			</div>
		</div>
	</g:if>

	<div class="panel panel-default">
		<div class="panel-heading">
			Parâmetros do Autorizador
		</div>

		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="maximoTrnPorDia">Máximo Transações *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="maximoTrnPorDia" id="maximoTrnPorDia" value="${rhInstance?.maximoTrnPorDia}"
							   min="0" required/>
						<span class="input-group-addon">por dia</span>
					</div>
				</div>

%{--
				<div class="form-group col-md-3">
					<label class="control-label" for="diasInatividade">Inatividade *</label>
					<div class="input-group">
						<input type="number" class="form-control" name="diasInatividade" id="diasInatividade" value="${rhInstance?.diasInatividade}"
							   min="0" required/>
						<span class="input-group-addon">dias</span>
					</div>
				</div>
--}%
			</div>
		</div>
	</div>


	<div class="panel-footer">
		<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
			<g:if test="${action == Util.ACTION_VIEW }">
				<g:link class="btn btn-default" action="edit" id="${rhInstance.id}"><span class="glyphicon glyphicon-edit"></span>
					<g:message code="default.button.edit.label" default="Edit"/>
				</g:link>
				<g:if test="${!roleRh}">
					<button name="_action_delete" type="submit" class="btn btn-danger"
							onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
						<span class="glyphicon glyphicon-remove"></span>&nbsp;Remover</button>
				</g:if>

			</g:if>
			<g:elseif test="${action == Util.ACTION_NEW}">
				<button name="_action_save" type="submit" class="btn btn-success" >
					<span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.create.label" default="Create"></g:message>
				</button>

			</g:elseif>
			<g:elseif test="${action == Util.ACTION_EDIT}">
				<button name="_action_update" type="submit" class="btn btn-success" >
					<span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.update.label" default="Update"></g:message>
				</button>
			</g:elseif>
		</sec:ifAnyGranted>

	</div>


</g:form>