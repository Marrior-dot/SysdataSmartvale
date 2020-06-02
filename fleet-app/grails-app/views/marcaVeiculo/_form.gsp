<div class="row">
    <div class="form-group col-md-6">
        <label for="nome"><g:message code="marcaVeiculo.nome.label" default="Nome"/></label>
        <g:textField class="form-control" name="nome" value="${marcaVeiculoInstance?.nome}" required="required"/>
    </div>
    <div class="form-group col-md-6">
        <label for="nome"><g:message code="marcaVeiculo.abreviacao.label" default="Abreviação"/></label>
        <input type="text" class="form-control" id="abreviacao" name="abreviacao" required
               value="${marcaVeiculoInstance?.abreviacao}" maxlength="6"/>
        <span id="helpBlock" class="help-block">Abreviação deve conter no máximo 6 caracteres.</span>
    </div>
</div>
