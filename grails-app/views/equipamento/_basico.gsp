<%@ page import="com.sysdata.gestaofrota.Equipamento" %>
<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>

<script type="text/javascript" src="${resource(dir:'js',file:'complementoNomeEmbossing.js') }"></script>

<div class="enter">							
	<g:form method="post" >
		<g:hiddenField name="id" value="${equipamentoInstance?.id}" />
		%{--<g:hiddenField name="version" value="${funcionarioInstance?.version}" />--}%
		<g:hiddenField name="version" value="${equipamentoInstance?.version}" />
		<g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
	    <g:hiddenField name="action" value="${action}" />
		<g:hiddenField name="tam-max-embossing" value="${tamMaxEmbossing}"/>

		<table class="table table-striped">
			<thead>
			<tr>
				<th>${"Empresa"}</th>
				<th>${"Centro de Custo"}</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td><g:link controller="rh" action="show" id="${unidadeInstance?.rh.id}">${unidadeInstance?.rh?.nome}</g:link></td>
				<td><g:link controller="unidade" action="show" id="${unidadeInstance?.id}">${unidadeInstance?.codigo} - ${unidadeInstance?.nome}</g:link></td>
			</tr>
			</tbody>
		</table>

		<g:set var="programaMaquina" value="${unidadeInstance?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.MAQUINA}"/>
		<g:hiddenField name="programa-maquina" value="${programaMaquina}"/>
		
		<div class="row">
			<div class="form-group col-md-3">
				<label for="codigo">Código</label>
				<g:textField class="form-control" name="codigo" value="${equipamentoInstance?.codigo}" maxlength="10"
							 onchange="updateNomeEmbossing('codigo', 'tipo.id')"/>
			</div>

			<div class="form-group col-md-3">
				<label for="tipo.id">Tipo</label>
				<g:set var="tipos" value="${TipoEquipamento.list()}"/>
				<g:select name="tipo.id" class="form-control" from="${tipos}" onchange="updateNomeEmbossing('codigo', 'tipo.id')"
						  optionKey="id" optionValue="nome" noSelection="${['null':'Selecione um tipo...']}"
						  value="${equipamentoInstance?.tipo?.id}"/>

				<g:select name="marcas.abreviacao" from="${tipos}" style="visibility: hidden;"
						  optionValue="abreviacao" optionKey="id"/>
			</div>

			<div class="form-group col-md-5">
				<label for="tipoAbastecimento">Tipo de Combustível</label>
				<g:select class="form-control" name="tipoAbastecimento"
						  from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}"
						  value="${equipamentoInstance?.tipoAbastecimento}"/>
			</div>
		</div>

		<div class="row">
			<div class="form-group col-md-2">
				<label for="capacidadeTanque">Capacidade Tanque</label>
				<div class="input-group">
					<input type="number" class="form-control maxValor" id="capacidadeTanque" name="capacidadeTanque"
						   value="${equipamentoInstance?.capacidadeTanque}" min="0" maxlength="5" required/>
					<span class="input-group-addon">litros</span>
				</div>
			</div>

			<div class="form-group col-md-4">
				<label for="mediaConsumo">Média de Consumo *</label>
				<div class="input-group">
					<input type="number" class="form-control maxValor" id="mediaConsumo" name="mediaConsumo"
						   value="${equipamentoInstance?.mediaConsumo}" min="0" maxlength="5" required/>
					<span class="input-group-addon">litros/dia</span>
				</div>
			</div>
		</div>

		<g:if test="${programaMaquina}">
			<div class="row">
				<div class="form-group col-md-6">
					<label for="complementoEmbossing">Nome Impresso no Cartão</label>
					<div class="input-group">
						<span class="input-group-addon" id="placa-modelo-addon">${equipamentoInstance?.codigo ?: "CODIGO"} ${equipamentoInstance?.tipo?.abreviacao ?: "TIPO"}</span>
						<g:set var="codigoTipoLength" value="${(equipamentoInstance?.codigo?.length() ?: 8) + (equipamentoInstance?.tipo?.abreviacao?.length() ?: 6) + 2}"/>

						<input type="text" class="form-control" id="complementoEmbossing" name="complementoEmbossing" maxlength="${tamMaxEmbossing - codigoTipoLength}"
							   placeholder="Digite aqui algum complemento (ex: nome equipamento)" value="${equipamentoInstance?.complementoEmbossing}" required/>
					</div>
					<span id="helpBlock" class="help-block">O campo acima pode conter no máximo <strong id="tam-max-embossing-str">${tamMaxEmbossing - codigoTipoLength}</strong> caracteres.</span>
				</div>

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
			</div>

			<div class="row">

				<div class="form-group col-md-2">
					<label for="portador.limiteTotal">Limite Total *</label>
					<div class="input-group">
						<input class="form-control money"
							   id="portador.limiteTotal" name="portador.limiteTotal"
							   value="${equipamentoInstance?.portador?.limiteTotal}" required/>
					</div>
				</div>

				<div class="form-group col-md-2">
					<label for="portador.limiteDiario">Limite Diário *</label>
					<div class="input-group">
						<input  class="form-control money"
								id="portador.limiteDiario" name="portador.limiteDiario"
								value="${equipamentoInstance?.portador?.limiteDiario}" />
					</div>
				</div>

				<div class="form-group col-md-2">
					<label for="portador.limiteMensal">Limite Mensal *</label>
					<div class="input-group">
						<input  class="form-control money"
								id="portador.limiteMensal" name="portador.limiteMensal"
								value="${equipamentoInstance?.portador?.limiteMensal}" />
					</div>
				</div>

			</div>
		</g:if>

		<div class="row">
			<div class="form-group col-md-12">
				<label for="descricao">Descrição</label>
				<g:textArea rows="4" class="form-control" name="descricao" value="${equipamentoInstance?.descricao}" size="60" maxlength="60"/>
			</div>
		</div>
		
	    <div class="buttons">
			<g:if test="${action=='novo'}">
				<span class="button"><g:actionSubmit class="btn btn-default" action="save" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
			</g:if>
          	<g:if test="${action=='editando'}">
               <span class="button"><g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
               <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
          	<g:if test="${action=='visualizando'}">
           	<span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
               <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
	    </div>
	</g:form>
</div>