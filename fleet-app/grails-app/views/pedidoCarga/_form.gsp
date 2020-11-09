<%@ page import="com.sysdata.gestaofrota.PedidoCargaProgramado; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Unidade; com.sysdata.gestaofrota.Rh; java.text.SimpleDateFormat; com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>


<g:hiddenField name="id" value="${pedidoCargaInstance?.id}"/>
<g:hiddenField name="action" value="${action}"/>
<g:hiddenField name="vinculoCartao" value="${pedidoCargaInstance?.unidade?.rh?.vinculoCartao}"/>


<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Empresa / Unidade</h3>
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="empresa">Empresa</label>
                <g:select name="empresa"
                          from="${Rh.ativosPrepago.list()}"
                          class="form-control"
                          value="${pedidoCargaInstance?.unidade?.rh?.id}"
                          optionKey="id"
                          optionValue="nome"
                          noSelection="['': '--Selecione uma Empresa--']"
                          dataAttrs="[vinculoCartao: 'vinculoCartao']"/>
            </div>
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="unidade">Unidade</label>
                <g:select name="unidade"
                          from="${Unidade.findAllByRh(pedidoCargaInstance?.unidade?.rh)}"
                          class="form-control"
                          value="${pedidoCargaInstance?.unidade?.id}"
                            optionKey="id"
                          optionValue="nome"
                          noSelection="['': '--Selecione uma Unidade--']"/>
            </div>
        </div>
    </div>
</div>


<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Dados Pedido</h3>
    </div>

    <div class="panel-body">
        <div class="row">

            <g:if test="${action == Util.ACTION_NEW}">
                <div class="col-md-3">
                    <g:checkBox name="pedidoProgramado" value="${pedidoCargaInstance?.instanceOf(PedidoCargaProgramado)}" /> Pedido Programado
                </div>

                <div class="col-md-3">
                    <g:radioGroup name="tipoTaxa" labels="['Taxa Administração', 'Taxa Desconto']" values="[1, 2]" value="1" >
                        <p>${it.radio} ${it.label}</p>
                    </g:radioGroup>
                </div>
            </g:if>

            <div class="col-md-3">
                <label class="control-label">${pedidoCargaInstance?.taxa ? 'Taxa Administração' : 'Taxa Desconto'}</label>
                <p id="taxaPedido" class="form-control-static">${pedidoCargaInstance?.taxa ?: pedidoCargaInstance.taxaDesconto}%</p>
            </div>

            <g:if test="${action != Util.ACTION_NEW && pedidoCargaInstance}">
                <div class="col-md-3">
                    <label class="control-label">Total Pedido ${pedidoCargaInstance?.taxa ? '(+ taxa)' : '(- taxa)'}</label>
                    <p class="form-control-static"> <g:formatNumber number="${pedidoCargaInstance?.total}" type="currency"/></p>
                </div>
            </g:if>
        </div>

        <div id="instancia"></div>

    </div>
</div>

<div id="programado"></div>


<g:render template="/categoriaFuncionario/list"
          model="${[categoriaFuncionarioInstanceList: pedidoCargaInstance?.perfisRecarga]}"/>

<g:if test="${! pedidoCargaInstance?.unidade || pedidoCargaInstance?.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO}">
    <div id="pedidoFuncionarios" style="display: none">
        <g:render template="funcionarios" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
    </div>
</g:if>

<g:if test="${! pedidoCargaInstance?.unidade || pedidoCargaInstance?.unidade?.rh?.vinculoCartao == TipoVinculoCartao.MAQUINA}">
    <div id="pedidoVeiculos" style="display: none">
        <g:render template="veiculos" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
    </div>
</g:if>



%{--<g:render template="taxasCartao" />--}%

<br/>