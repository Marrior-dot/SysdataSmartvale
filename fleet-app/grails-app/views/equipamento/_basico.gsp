<%@ page import="com.sysdata.gestaofrota.CategoriaFuncionario; com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Equipamento" %>
<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>

<div class="panel panel-default">

	<div class="panel-heading">Dados Básicos</div>

	<g:form method="post" >
	<div class="panel-body">

			<g:hiddenField name="id" value="${equipamentoInstance?.id}" />
			<g:hiddenField name="version" value="${equipamentoInstance?.version}" />

			<g:hiddenField name="unidade.id" value="${equipamentoInstance?.unidade?.id}"/>

			<g:if test="${equipamentoInstance?.unidade?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.MAQUINA}">
				<g:hiddenField name="portador.unidade.id" value="${equipamentoInstance?.portador?.unidade?.id}"/>
			</g:if>

			<g:hiddenField name="action" value="${action}" />
			<g:hiddenField name="tam-max-embossing" value="${tamMaxEmbossing}"/>

			<g:set var="programaMaquina" value="${equipamentoInstance?.unidade?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.MAQUINA}"/>
			<g:hiddenField name="programa-maquina" value="${programaMaquina}"/>

			<div class="row">
				<div class="form-group col-md-3">
					<label for="codigo">Código</label>
					<g:textField class="form-control editable" name="codigo" value="${equipamentoInstance?.codigo}" maxlength="10" required=""
								 onchange="updateNomeEmbossing('codigo', 'tipo.id')"/>
				</div>

				<div class="form-group col-md-3">
					<label for="tipo.id">Tipo</label>
					<g:set var="tipos" value="${TipoEquipamento.list()}"/>
					<g:select name="tipo.id" class="form-control editable" from="${tipos}" onchange="updateNomeEmbossing('codigo', 'tipo.id')"
							  optionKey="id" optionValue="nome" noSelection="${['null':'Selecione um tipo...']}"
							  required=""
							  value="${equipamentoInstance?.tipo?.id}"/>

					<g:select name="marcas.abreviacao" from="${tipos}" style="visibility: hidden;"
							  optionValue="abreviacao" optionKey="id"/>
				</div>

				<div class="form-group col-md-5">
					<label for="tipoAbastecimento">Tipo de Combustível</label>
					<g:select class="form-control editable" name="tipoAbastecimento"
							  from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}"
							  optionValue="nome"
							  value="${equipamentoInstance?.tipoAbastecimento}"/>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-3">
					<label for="capacidadeTanque">Capacidade Tanque</label>
					<div class="input-group">
						<input type="number" class="form-control maxValor editable" id="capacidadeTanque" name="capacidadeTanque"
							   value="${equipamentoInstance?.capacidadeTanque}" min="0" maxlength="5" required/>
						<span class="input-group-addon">litros</span>
					</div>
				</div>

				<div class="form-group col-md-3">
					<label for="mediaConsumo">Média de Consumo *</label>
					<div class="input-group">
						<input type="number" class="form-control maxValor editable" id="mediaConsumo" name="mediaConsumo"
							   value="${equipamentoInstance?.mediaConsumo}" min="0" maxlength="5" required/>
						<span class="input-group-addon">litros/dia</span>
					</div>
				</div>

			<g:if test="${programaMaquina}">
				<div class="form-group col-md-6">
					<label for="complementoEmbossing">Nome Impresso no Cartão</label>
					<div class="input-group">
						<span class="input-group-addon" id="placa-modelo-addon">${equipamentoInstance?.codigo ?: "CODIGO"} ${equipamentoInstance?.tipo?.abreviacao ?: "TIPO"}</span>
						<g:set var="codigoTipoLength" value="${(equipamentoInstance?.codigo?.length() ?: 8) + (equipamentoInstance?.tipo?.abreviacao?.length() ?: 6) + 2}"/>

						<input type="text" class="form-control editable" id="complementoEmbossing" name="complementoEmbossing" maxlength="${tamMaxEmbossing - codigoTipoLength}"
							   placeholder="Digite aqui algum complemento (ex: nome equipamento)" value="${equipamentoInstance?.complementoEmbossing}" required/>
					</div>
					<span id="helpBlock" class="help-block">O campo acima pode conter no máximo <strong id="tam-max-embossing-str">${tamMaxEmbossing - codigoTipoLength}</strong> caracteres.</span>
				</div>

			</g:if>



			</div>

			<g:if test="${programaMaquina}">
				<div class="row">

					%{--<div class="form-group col-md-3">
                        <label for="portador.valorLimite">Limite *</label>
                        <div class="input-group">
                            <input type="number" min="0" step="0.01" class="form-control money"
                                   id="portador.valorLimite" name="portador.valorLimite"
                                   value="${equipamentoInstance?.portador?.limiteTotal}" required/>
                        </div>
                    </div>--}%

					%{--<div class="form-group col-md-3">
                        <label for="portador.tipoLimite">Tipo Limite *</label>
                        <g:select name="portador.tipoLimite" from="${com.sysdata.gestaofrota.TipoLimite.values()}"
                                  required="required" value="${equipamentoInstance?.portador?.tipoLimite}"
                                  class="form-control" optionValue="nome"/>
                    </div>--}%


				<g:if test="${equipamentoInstance?.unidade?.rh.modeloCobranca == TipoCobranca.PRE_PAGO}" >
					<div class="form-group col-md-3">
						<label for="categoria.id">Perfil de Recarga *</label>
						<g:select name="categoria.id" from="${CategoriaFuncionario.porUnidade(equipamentoInstance?.unidade)?.list()}"
								  value="${equipamentoInstance?.categoria?.id}" required="required"
								  noSelection="${['null': 'Selecione a categoria...']}"
								  optionKey="id" class="form-control editable" optionValue="nome"/>
					</div>
				</g:if>


				<g:if test="${equipamentoInstance?.unidade?.rh.modeloCobranca == TipoCobranca.POS_PAGO}">

						<div class="form-group col-md-2">
							<label for="portador.limiteTotal">Limite Total *</label>
							<div class="input-group">
								<input class="form-control money editable"
									   id="portador.limiteTotal" name="portador.limiteTotal"
									   value="${equipamentoInstance?.portador?.limiteTotal}" required/>
							</div>
						</div>

				</g:if>

				</div>

			</g:if>

			<div class="row">
				<div class="form-group col-md-12">
					<label for="descricao">Descrição</label>
					<g:textArea rows="4" class="form-control editable" name="descricao" value="${equipamentoInstance?.descricao}" size="60" maxlength="60"/>
				</div>
			</div>


	</div>

	<div class="panel-footer">
		<g:if test="${action=='novo'}">
			<button name="_action_save" class="btn btn-success">
				<i class="glyphicon glyphicon-floppy-save"></i>
				<g:message code="default.button.update.label" default="Update"></g:message>
			</button>
		</g:if>
		<g:if test="${action=='editando'}">
			<button name="_action_update" class="btn btn-success">
				<i class="glyphicon glyphicon-floppy-save"></i>
				<g:message code="default.button.update.label" default="Update"></g:message>
			</button>
			<button name="_action_delete" class="btn btn-danger">
				<i class="glyphicon glyphicon-remove"></i>
				<g:message code="default.button.delete.label" default="Delete"></g:message>
			</button>
			%{--<g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />--}%
		</g:if>
		<g:if test="${action=='visualizando'}">
			<button name="_action_edit" class="btn btn-default">
				<i class="glyphicon glyphicon-edit"></i>
				<g:message code="default.button.edit.label" default="Edit"></g:message>
			</button>
			<button name="_action_delete" class="btn btn-danger">
				<i class="glyphicon glyphicon-remove"></i>
				<g:message code="default.button.delete.label" default="Delete"></g:message>
			</button>
		</g:if>
	</div>

	</g:form>

</div>