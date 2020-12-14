<%@ page import="com.sysdata.gestaofrota.TipoContrato; com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Util" %>

<g:form method="post" >

    <g:hiddenField name="id" value="${rhInstance?.id}" />
    <g:hiddenField name="version" value="${rhInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>
    <g:hiddenField name="editable" value="${editable}"/>

	<div class="panel panel-default">
		<div class="panel-heading">
			Dados Básicos
		</div>

		<div class="panel-body">
			<div class="row">
				<div class="col-md-6">
					<bs:formField id="cnpj" name="cnpj" label="CNPJ" class="cnpj ${editable ? 'editable': ''}" required="true" value="${rhInstance?.cnpj}" />
				</div>

				<g:set var="bloquearModCob" value="${action == Util.ACTION_EDIT && (rhInstance?.funcionariosCount > 0 || rhInstance?.veiculosCount > 0)}"/>
				<div class="form-group col-md-6 ${bloquearModCob ? 'has-warning' : ''}">
					<label class="control-label" for="modeloCobranca">Modelo Cobrança *</label>
					<g:select name="modeloCobranca" from="${TipoCobranca.values()}" class="form-control ${editable ? 'editable': ''}"
							  optionValue="nome" value="${rhInstance?.modeloCobranca}" onchange="alterarModeloCobranca()"
							  aria-describedby="modelo-cobranca" disabled="${bloquearModCob}" required="required"/>
				<g:if test="${bloquearModCob}">
					<span id="modelo-cobranca" class="help-block">Não é possível alterar o Modelo Cobrança. Você já possui funcionários/veiculos cadastrados.</span>
				</g:if>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<bs:formField class="form-control ${editable ? 'editable': ''}" id="nome" label="Razão Social" required="true" value="${rhInstance?.nome}" />
				</div>

				<div class="col-md-6">
					<bs:formField class="form-control ${editable ? 'editable': ''}" id="nomeFantasia" label="Nome Fantasia" required="true" value="${rhInstance?.nomeFantasia}" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<bs:formField class="form-control ${editable ? 'editable': ''}" id="email" label="Email" value="${rhInstance?.email}" />
				</div>
			</div>


			<div class="panel panel-default">
				<div class="panel-heading">
					Contrato
				</div>

				<div class="panel-body">

					<div class="row">

						<div class="form-group col-md-6">
							<label class="control-label" for="tipoContrato">Tipo Contrato</label>
							<g:select name="tipoContrato" from="${TipoContrato.values()}" class="form-control ${editable ? 'editable': ''}"
									  noSelection="[null: '--Selecione o Tipo de Contrato--']"
									  optionValue="nome" value="${rhInstance?.tipoContrato}" required="required"/>
						</div>

						<div class="form-group col-md-3">
							<label for="dataInicioContrato">Data Início</label>
							<g:textField name="dataInicioContrato" class="form-control datepicker ${editable ? 'editable': ''}"
										 value="${Util.formattedDate(rhInstance?.dataInicioContrato)}"></g:textField>
						</div>
						<div class="form-group col-md-3">
							<label for="dataFimContrato">Data Fim</label>
							<g:textField name="dataFimContrato" class="form-control datepicker ${editable ? 'editable': ''}"
										 value="${Util.formattedDate(rhInstance?.dataFimContrato)}"></g:textField>
						</div>

					</div>

				</div>

			</div>

		</div>

	</div>

	<div class="panel panel-default">
		<div class="panel-heading">Cartão</div>

		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-6 ${bloquearModCob ? 'has-warning' : ''} ${editable ? 'editable': ''}">
					<label class="control-label" for="vinculoCartao">Vincular Cartão a:</label>

					<div class="input-group">
						<g:if test="${action == Util.ACTION_VIEW}">
							<input type="text" class="form-control ${editable ? 'editable': ''}" name="vinculoCartao" id="vinculoCartao" disabled
								   value="${rhInstance?.vinculoCartao}" aria-describedby="vinculo-cartao"/>
						</g:if>
						<g:else>
							<g:select name="vinculoCartao" from="${com.sysdata.gestaofrota.TipoVinculoCartao.values()}"
									  disabled="${bloquearModCob}" class="form-control ${editable ? 'editable': ''}" aria-describedby="vinculo-cartao"
									  optionKey="key" value="${rhInstance?.vinculoCartao}"/>
						</g:else>
						<span class="input-group-addon">
							<input type="checkbox" name="cartaoComChip" id="cartaoComChip" ${rhInstance.cartaoComChip || action == Util.ACTION_NEW ? 'checked' : ''} ${bloquearModCob ? 'disabled': ''}>
							<strong>Cartão com chip</strong>
						</span>
						%{--
                                                <span class="input-group-addon">
                                                    <input type="checkbox" id="renovarLimite" name="renovarLimite" ${rhInstance?.renovarLimite ? 'checked' : ''} ${bloquearModCob ? 'disabled': ''}>
                                                    <strong>Renovar Limite</strong>
                                                </span>
                        --}%
					</div>
					<g:if test="${bloquearModCob}">
						<span id="vinculo-cartao" class="help-block">Não é possível alterar o Vinculo Cartão. Você já possui funcionários/veiculos cadastrados.</span>
					</g:if>
				</div>



			</div>
		</div>
	</div>

	<div class="panel panel-default" id="limites">
		<div class="panel-heading">Limites</div>

		<div class="panel-body">
			<div class="form-group col-md-3">
				<label class="control-label" for="limiteTotal">LimiteTotal *</label>
				<div class="input-group">
					<g:textField name="limiteTotal" class="form-control money ${editable ? 'editable': ''}"
								 value="${Util.formatCurrency(rhInstance?.limiteTotal)}" required="true"></g:textField>
				</div>
			</div>

			<div class="form-group col-md-3">
				<label class="control-label">Comprometido c/ Cartões</label>
				<h5>${Util.formatCurrency(rhInstance?.limiteComprometido)}</h5>
			</div>

			<div class="form-group col-md-3">
				<label class="control-label">Limite Disponível</label>
				<h5>${Util.formatCurrency((rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO) ? rhInstance?.limiteDisponivel : rhInstance?.saldoDisponivel)}</h5>
			</div>
			<div class="form-group col-md-3">
				<label class="control-label">Saldo Cartões Disponível</label>
				<h5>${Util.formatCurrency(rhInstance?.saldoDisponivelCartoes)}</h5>
			</div>
		</div>
	</div>

	<div class="panel panel-default" id="pedido-carga">
		<div class="panel-heading">
			Pedido de Carga
		</div>

		<div class="panel-body">
			<div class="row">
%{--
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaPedido">Taxa Pedido *</label>
					<div class="input-group">
						<input type="text" class="form-control percentual" name="taxaPedido" id="taxaPedido"
							   value="${Util.formatPercentage(rhInstance?.taxaPedido)}" min="0"  required/>
						<span class="input-group-addon">%</span>
					</div>
				</div>
--}%

				<div class="form-group col-md-3">
					<label class="control-label" for="validadeCarga">Validade Carga *</label>
					<div class="input-group">
						<input type="number" class="form-control ${editable ? 'editable': ''}" name="validadeCarga" id="validadeCarga" value="${rhInstance?.validadeCarga}"
							   min="0" required/>
						<span class="input-group-addon">dias</span>
					</div>
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
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaAdministracao">Taxa de Administração *</label>
					<div class="input-group">

						<g:textField name="taxaAdministracao" class="form-control percentual ${editable ? 'editable': ''}"
									 value="${Util.formatPercentage(rhInstance?.taxaAdministracao)}">
							required
						</g:textField>
						<span class="input-group-addon">%</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label class="control-label" for="taxaDesconto">Taxa de Desconto *</label>
					<div class="input-group">
						<g:textField name="taxaDesconto" class="form-control percentual ${editable ? 'editable': ''}"
									 value="${Util.formatPercentage(rhInstance?.taxaDesconto)}">
							required
						</g:textField>
						<span class="input-group-addon">%</span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="panel panel-default">
		<div class="panel-heading">
			Outras Taxas
		</div>

		<div class="panel-body">
			<div class="row">
				<div class="form-group col-md-3">
					<label class="control-label" for="taxaUtilizacao">Utilização *</label>
					<div class="input-group">
						<span class="input-group-addon">R$</span>
						<input type="number" class="form-control ${editable ? 'editable': ''}" name="taxaUtilizacao" id="taxaUtilizacao"
							   value="${rhInstance?.taxaUtilizacao}" min="0" step="0.01" required/>
					</div>
				</div>


				<div class="form-group col-md-3">
					<label class="control-label" for="taxaManutencao">Taxa de Manutenção *</label>
					<div class="input-group">
						<input type="number" class="form-control ${editable ? 'editable': ''}" name="taxaManutencao" id="taxaManutencao"
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




	<div id="fatura">

		<div class="panel panel-default">
			<div class="panel-heading">Fatura</div>
			<div class="panel-body">
				<div class="row">
					<div class="form-group col-md-3">
						<label class="control-label" for="prazoPgtFatura">Dias p/ Vencimento após Corte *</label>
						<div class="input-group">
							<input type="number" class="form-control ${editable ? 'editable': ''}" name="prazoPgtFatura" id="prazoPgtFatura"
								   value="${rhInstance?.prazoPgtFatura}" min="0" required/>
							<span class="input-group-addon">dias</span>
						</div>
					</div>

					<div class="form-group col-md-3">
						<label class="control-label" for="diasToleranciaAtraso">Dias de Tolerância a Atraso *</label>
						<div class="input-group">
							<input id="diasToleranciaAtraso" name="diasToleranciaAtraso" type="number" class="form-control ${editable ? 'editable': ''}"
								   min="0" value="${rhInstance?.diasToleranciaAtraso}" required>
							<span class="input-group-addon">dias</span>
						</div>
					</div>

					<div class="form-group col-md-3">
						<label class="control-label" for="multaAtraso">Multa por Atraso *</label>
						<div class="input-group">
							<input type="text" class="form-control percentual" name="multaAtraso ${editable ? 'editable': ''}" id="multaAtraso"
								   value="${Util.formatPercentage(rhInstance?.multaAtraso)}" required/>
							<span class="input-group-addon">%</span>
						</div>
					</div>

					<div class="form-group col-md-3">

						<label class="control-label" for="jurosProRata">Juros Pró-Rata por Atraso *</label>
						<div class="input-group">
							<input type="text" class="form-control percentual ${editable ? 'editable': ''}" name="jurosProRata" id="jurosProRata"
								   value="${Util.formatPercentage(rhInstance?.jurosProRata)}" min="0"  required/>
							<span class="input-group-addon">%</span>
						</div>
					</div>

				</div>
			</div>
		</div>



	</div>


	<g:render template="/endereco/form" model="[enderecoInstance: rhInstance?.endereco, endereco:'endereco', legend:'Endereço']"/>

	<g:render template="/telefone/form" model="[telefoneInstance: rhInstance?.telefone,telefone:'telefone', legend:'Telefone']"/>


	<dyn:props tipoParticipante="EMPRESA_RH" participante="${rhInstance}"></dyn:props>



	<div class="panel-footer">
		<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">
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