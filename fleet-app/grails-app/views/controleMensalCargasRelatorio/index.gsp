<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Controle Mensal de Cargas"/>
    <title>${relatorio}</title>

    <export:resource />
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>${relatorio}</h4>
    </div>
    <div class="panel-body">

        <g:link uri="/" class="btn btn-default">
            <span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/>
        </g:link>

        <g:form action="index">
            <div class="panel panel-default panel-top">
                <div class="panel-heading">Pesquisa</div>

                <div class="panel-body">

                    <g:render template="/components/rhUnidadeSelect"></g:render>

                    <div class="row">
                        <div class="col-md-3">
                            <label class="control-label" for="dataInicio">Data Inicial</label>
                            <g:textField name="dataInicio" class="form-control datepicker" value="${params.dataInicio}"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label class="control-label" for="dataFim">Data Final</label>
                            <g:textField name="dataFim" class="form-control datepicker" value="${params.dataFim}"></g:textField>
                        </div>
                    </div>

                </div>


                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>

        </g:form>

        <!--<div class="table-responsive">-->

        <table class="table table-bordered table-stripped table-responsive ">
            <thead>
            <th>Cliente</th>
            <th>Unidade</th>
            <th>Codigo Pedido</th>
            <th>Data Pedido</th>
            <th>Data da Carga</th>
            <!--<th>Status</th>-->
            <th>Placa/Cod Equip.</th>
            <th>Valor Carga</th>
            <!--<th>Total Pedido (- taxa)</th>
            <th>Taxa(%)</th>
            <th>Taxa Desconto</th>-->
            <th>Validade(dias) </th>

            </thead>
            <tbody>
            <g:each in="${controleMensalCargasList}" var="carga">
                <tr>
                    <td>${carga.cliente}</td>
                    <td>${carga.unidade}</td>
                    <td>${carga.pedidoId}</td>
                    <td><g:formatDate date="${carga.pedidoDataCriacao}" format="dd/MM/yy"/></td>
                    <td><g:formatDate date="${carga.pedidoDataCarga}" format="dd/MM/yy"/></td>
                    %{--<td>${carga.pedido.status}</td>--}%
                    <td>${carga.identificadorMaquina}</td>
                    <td>${carga?.valor}
                    <!--<td>${carga?.pedidoTotal}</td>
                    <td>${carga?.pedidoTaxa}</td>
                    <td>${carga.pedidoTaxaDesconto}</td>-->
                    <td>${carga.pedidoValidade}</td>
                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>
        <!--</div>-->

        <g:paginate total="${controleMensalCargasCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" params="${params}"/>
    </div>
</div>


</body>
</html>