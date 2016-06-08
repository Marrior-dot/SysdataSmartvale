<%@ page import="com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post" >
    <g:hiddenField name="id" value="${veiculoInstance?.id}" />
    <g:hiddenField name="version" value="${funcionarioInstance?.version}" />
    <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
    <g:hiddenField name="action" value="${action}" />

    <table class="table" style="border:1px solid;border-color: #DDD;font-size:14px;">
        <thead>
        <tr>
            <th>${message(code: 'rh.label', default: 'RH')}</th>
            <th>${message(code: 'unidade.label', default: 'Unidade')}</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${unidadeInstance?.rh.nome}</td>
            <td>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</td>
        </tr>
        </tbody>
    </table>

    <div class="panel panel-default">
        <div class="panel-heading">Dados Básicos</div>
        <div class="panel-body">

            %{--<fieldset style="border:1px solid;font-size:14px;">
                <label><span>RH</span>${unidadeInstance?.rh.nome}</label>
                <label><span>Unidade</span>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</label>
                <div class="clear"></div>
            </fieldset>
--}%

            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="placa" name="placa" label="Placa" value="${veiculoInstance?.placa}"></bs:formField>
                </div>
                <div class="col-xs-4">
                    <label for="marca.id">MARCA</label>
                    <g:select name="marca.id" from="${MarcaVeiculo.list()}" optionKey="id" optionValue="nome"
                        class="form-control"
                              noSelection="${['null':'Selecione uma marca...']}" value="${veiculoInstance?.marca?.id}"/>
                </div>
                <div class="col-xs-4">
                    <bs:formField id="modelo" name="modelo" label="Modelo" value="${veiculoInstance?.modelo}"></bs:formField>
                </div>
                <div class="col-xs-4">
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
                        <g:select name="tipoAbastecimento" from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}"
                                  class="form-control"
                                  value="${veiculoInstance?.tipoAbastecimento}"  />
                </div>

                <div class="col-xs-4">
                    <label for="validadeExtintor">Validade Extintor</label>
                    <input type="text" class="form-control datepicker" id="validadeExtintor" name="validadeExtintor" value="${Util.formattedDate(veiculoInstance?.validadeExtintor)}"/>
                </div>
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