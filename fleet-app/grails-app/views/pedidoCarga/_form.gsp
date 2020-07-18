<%@ page import="com.sysdata.gestaofrota.Unidade; com.sysdata.gestaofrota.Rh; java.text.SimpleDateFormat; com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>


<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Empresa / Unidade</h3>
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="empresa">Empresa</label>
                <g:select name="empresa"
                          from="${Rh.ativos.list()}" class="form-control"
                          value="${pedidoCargaInstance?.unidade?.rh?.id}"
                          optionKey="id"
                          optionValue="nome" noSelection="['': '--Selecione uma Empresa--']"
                          dataAttrs="[vinculoCartao: 'vinculoCartao']"/>
            </div>
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="unidade">Unidade</label>
                <g:select name="unidade" from="${Unidade.list(order: 'nome')}" class="form-control" value="${pedidoCargaInstance?.unidade?.id}"
                    optionKey="id" optionValue="nome" noSelection="['': '--Selecione uma Unidade--']"/>
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

            <div class="col-xs-3 input-group-sm">
                <label class="control-label" for="dataCarga">Data de Carga</label>
                <input type="text" id="dataCarga" name="dataCarga" class="form-control datepicker"
                       value="${Util.formattedDate(pedidoCargaInstance?.dataCarga)}"/>
            </div>

            <div class="col-xs-3 input-group-sm">
                <label class="control-label">Taxa Pedido</label>
                <p id="taxaPedido" class="form-control-static">${pedidoCargaInstance?.taxa ?: 0}%</p>
            </div>

            <g:if test="${action != Util.ACTION_NEW && pedidoCargaInstance}">
                <div class="col-xs-3 input-group-sm">
                    <label class="control-label">Total Pedido (+ taxa)</label>
                    <p class="form-control-static"> <g:formatNumber number="${pedidoCargaInstance?.total}" type="currency"/></p>
                </div>
                <div class="col-xs-3 input-group-sm">
                    <label class="control-label">Status</label>

                    <%
                        def labelClass
                        switch (pedidoCargaInstance?.status) {
                            case StatusPedidoCarga.NOVO:
                                labelClass = "label-primary"
                                break
                            case StatusPedidoCarga.LIBERADO:
                                labelClass = "label-info"
                                break
                            case StatusPedidoCarga.FINALIZADO:
                                labelClass = "label-success"
                                break
                            case StatusPedidoCarga.CANCELADO:
                                labelClass = "label-danger"
                                break
                            default:
                                labelClass = "label-default"
                                break
                        }
                    %>

                    <h5><span class="label ${labelClass}">${pedidoCargaInstance?.status?.nome}</span></h5>

                </div>
            </g:if>
        </div>
    </div>
</div>

<g:render template="/categoriaFuncionario/list"
          model="${[categoriaFuncionarioInstanceList: pedidoCargaInstance?.categoriasFuncionario]}"/>


<div id="pedidoFuncionarios" style="display: none">
    <g:render template="funcionarios" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
</div>

<div id="pedidoVeiculos" style="display: none">
    <g:render template="veiculos" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
</div>

%{--<g:render template="taxasCartao" />--}%

<br/>