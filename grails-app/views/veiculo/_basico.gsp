<%@ page import="com.sysdata.gestaofrota.ModeloMaquina; com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Veiculo" %>
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
                    <bs:formField id="placa" class="placa" name="placa" label="Placa" value="${veiculoInstance?.placa}" onchange="updateNomeEmbossing('placa', 'marca.id')"></bs:formField>
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
                    <bs:formField id="modelo" name="modelo" label="Modelo" value="${veiculoInstance?.modelo}"></bs:formField>
                </div>
                <div class="col-xs-3">
                    <bs:formField id="ano" name="anoFabricacao" label="Ano Fabricação" value="${veiculoInstance?.anoFabricacao}"></bs:formField>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="chassi" name="chassi" label="Chassi" value="${veiculoInstance?.chassi}"></bs:formField>
                </div>
                <div class="col-xs-4">
                    <bs:formField id="capacidadeTanque" name="capacidadeTanque" label="Capacidade Tanque (lt)" value="${veiculoInstance?.capacidadeTanque}"></bs:formField>
                </div>
                <div class="col-xs-4">
                    <bs:formField id="autonomia" name="autonomia" label="Autonomia (Km/l)" value="${veiculoInstance?.autonomia}"></bs:formField>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">
                    <label for="tipoAbastecimento">Tipo de Combustível</label>
                        <g:select name="tipoAbastecimento" optionValue="nome" class="form-control"
                                  from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}"
                                  value="${veiculoInstance?.tipoAbastecimento}"/>
                </div>

                <div class="col-xs-4">
                    <label for="validadeExtintor">Validade Extintor</label>
                    <input type="text" class="form-control datepicker" id="validadeExtintor" name="validadeExtintor"
                           value="${Util.formattedDate(veiculoInstance?.validadeExtintor)}"/>
                </div>
                <g:if test="${programaMaquina}">
                    <div class="form-group col-xs-4">
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