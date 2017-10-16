<div class="row">
	<div class="form-group col-md-6">
		<label for="nome">Nome</label>
		<g:textField name="nome" class="form-control" value="${tipoEquipamentoInstance?.nome}" required="required"/>
	</div>
	<div class="form-group col-md-6">
		<label for="abreviacao">Abreviação</label>
		<g:textField name="abreviacao" class="form-control" value="${tipoEquipamentoInstance?.abreviacao}" required="required" maxlength="6"/>
		<span id="helpBlock" class="help-block">Maximo de caracteres permitidos: 6</span>
	</div>
</div>