<%@ page import="com.sysdata.gestaofrota.ModeloMaquina; com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Veiculo; com.sysdata.gestaofrota.TipoVinculoCartao" %>
<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<script type="text/javascript" src="${resource(dir:'js',file:'complementoNomeEmbossing.js') }"></script>

<g:form method="post" >
    <g:hiddenField name="id" value="${veiculoInstance?.id}" />
    <g:hiddenField name="version" value="${funcionarioInstance?.version}" />
    <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
    <g:hiddenField name="action" value="${action}"/>
    <g:hiddenField name="tam-max-embossing" value="${tamMaxEmbossing}"/>

    <table class="table">
        <thead>
        <tr>
            <th>${message(code: 'rh.label', default: 'RH')}</th>
            <th>${message(code: 'unidade.label', default: 'Unidade')}</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><g:link controller="rh" action="show" id="${unidadeInstance?.rh.id}">${unidadeInstance?.rh?.nome}</g:link></td>
            <td><g:link controller="unidade" action="show" id="${unidadeInstance?.id}">${unidadeInstance?.codigo} - ${unidadeInstance?.nome}</g:link></td>
        </tr>
        </tbody>
    </table>

    <div class="panel panel-default">
        <div class="panel-heading">Dados Básicos</div>
        <div class="panel-body">
            <g:set var="programaMaquina" value="${unidadeInstance?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.MAQUINA}"/>
            <g:hiddenField name="programa-maquina" value="${programaMaquina}"/>

            <div class="row">
                <div class="col-xs-3">
                    <bs:formField id="placa" class="placa" name="placa" label="Placa" required="required"
                                  value="${veiculoInstance?.placa}" onchange="updateNomeEmbossing('placa', 'marca.id')"></bs:formField>
                </div>
                <div class="col-xs-3">
                    <label for="marca.id">MARCA</label>
                    <g:set var="marcas" value="${MarcaVeiculo.list(sort: 'nome')}"/>
                    <g:select name="marca.id" from="${marcas}" optionKey="id"
                              optionValue="nome" class="form-control" onchange="updateNomeEmbossing('placa', 'marca.id')"
                              noSelection="${['': 'Selecione uma marca...']}" value="${veiculoInstance?.marca?.id}"/>

                    <g:select name="marcas.abreviacao" from="${marcas}" style="visibility: hidden;"
                              optionValue="abreviacao" optionKey="id"/>
                </div>
                <div class="col-xs-3">
                    <bs:formField id="modelo" name="modelo" label="Modelo" required="required" value="${veiculoInstance?.modelo}"></bs:formField>
                </div>
                <div class="col-xs-3">
                    <bs:formField id="ano" name="anoFabricacao" label="Ano Fabricação" required="required" value="${veiculoInstance?.anoFabricacao}"></bs:formField>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-md-3">
                    <label class="control-label" for="capacidadeTanque">Chassi *</label>
                    <input type="text" class="form-control" id="chassi" name="chassi" value="${veiculoInstance?.chassi}" maxlength="30" required/>
                </div>

                <div class="form-group col-md-3">
                    <label class="control-label" for="capacidadeTanque">Capacidade Tanque *</label>
                    <div class="input-group">
                        <input id="capacidadeTanque" name="capacidadeTanque" type="number" class="form-control"
                               value="${veiculoInstance?.capacidadeTanque}" min="0" maxlength="5" required>
                        <span class="input-group-addon">litros</span>
                    </div>
                </div>

                <div class="form-group col-md-3">
                    <label class="control-label" for="autonomia">Autonomia *</label>
                    <div class="input-group">
                        <input id="autonomia" name="autonomia" type="number" class="form-control"
                               value="${veiculoInstance?.autonomia}" min="0" maxlength="5" required>
                        <span class="input-group-addon">Km/l</span>
                    </div>

                </div>

                <div class="form-group col-md-3">
                    <label for="tipoAbastecimento">Tipo de Combustível</label>
                    <g:select name="tipoAbastecimento" optionValue="nome" class="form-control"
                              from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}"
                              value="${veiculoInstance?.tipoAbastecimento}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <label for="validadeExtintor">Validade Extintor *</label>
                    <input type="text" class="form-control datepicker" id="validadeExtintor" name="validadeExtintor"
                           value="${Util.formattedDate(veiculoInstance?.validadeExtintor)}" required/>
                </div>

                <g:if test="${programaMaquina}">
                    <div class="form-group col-md-5">
                        <label for="complementoEmbossing">Nome Impresso no Cartão</label>
                        <div class="input-group">
                            <span class="input-group-addon" id="placa-modelo-addon">${veiculoInstance?.placa ?: "PLACA"} ${veiculoInstance?.marca?.abreviacao ?: "MODELO"}</span>
                            <g:set var="placaMarcaLength" value="${(veiculoInstance?.placa?.length() ?: 8) + (veiculoInstance?.marca?.abreviacao?.length() ?: 6) + 2}"/>

                            <input type="text" class="form-control" id="complementoEmbossing" name="complementoEmbossing" maxlength="${tamMaxEmbossing - placaMarcaLength}"
                                   placeholder="Digite aqui algum complemento (ex: modelo)" value="${veiculoInstance?.complementoEmbossing}" required/>
                        </div>
                        <span id="helpBlock" class="help-block">O campo acima pode conter no máximo <strong id="tam-max-embossing-str">${tamMaxEmbossing - placaMarcaLength}</strong> caracteres.</span>
                    </div>

                </g:if>
            </div>


            <g:if test="${unidadeInstance?.rh.vinculoCartao==TipoVinculoCartao.MAQUINA}">
                <div class="row">

                    <div class="form-group col-md-2">
                        <label for="portador.limiteTotal">Limite Total *</label>
                        <div class="input-group">
                            <span class="input-group-addon">R$</span>
                            <input type="number" min="0" step="0.01" class="form-control"
                                   id="portador.limiteTotal" name="portador.limiteTotal"
                                   value="${veiculoInstance?.portador?.limiteTotal}" required/>
                        </div>
                    </div>

                    <div class="form-group col-md-2">
                        <label for="portador.limiteDiario">Limite Diário *</label>
                        <div class="input-group">
                            <span class="input-group-addon">R$</span>
                            <input type="number" min="0" step="0.01" class="form-control"
                                   id="portador.limiteDiario" name="portador.limiteDiario"
                                   value="${veiculoInstance?.portador?.limiteDiario}" required/>
                        </div>
                    </div>

                    <div class="form-group col-md-2">
                        <label for="portador.limiteMensal">Limite Mensal *</label>
                        <div class="input-group">
                            <span class="input-group-addon">R$</span>
                            <input type="number" min="0" step="0.01" class="form-control"
                                   id="portador.limiteMensal" name="portador.limiteMensal"
                                   value="${veiculoInstance?.portador?.limiteMensal}" required/>
                        </div>
                    </div>

                </div>

            </g:if>



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