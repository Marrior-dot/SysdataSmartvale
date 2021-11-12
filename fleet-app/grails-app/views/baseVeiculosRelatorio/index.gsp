<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Base de Veículos"/>
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
                            <label class="control-label" for="placa">Placa</label>
                            <g:textField name="placa" class="form-control placa" value="${params.placa}"></g:textField>
                        </div>

                    </div>

                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>

        <div class="table-responsive">

        <table class="table table-bordered table-stripped">
            <thead>
            <th>Cliente</th>
            <th>Unidade</th>
            <th>Placa</th>
            <th>Marca/Modelo</th>
            <th>Chassi</th>
            <th>Ano Fabricação</th>
            <th>Cap Tanque(lts)</th>
            <th>Tipo Comb</th>
            <th>Saldo</th>

            </thead>
            <tbody>


            <g:each in="${baseVeiculosList}" var="veic">
                <tr>
                    <td>${veic.unidade.rh.nomeFantasia}</td>
                    <td>${veic.unidade.nome}</td>
                    <td>${veic.placa}</td>
                    <td>${veic.marca} / ${veic.modelo} </td>
                    <td>${veic.chassi}</td>
                    <td>${veic.anoFabricacao}</td>
                    <td>${veic?.capacidadeTanque}</td>
                    <td>${veic?.tipoAbastecimento}</td>
                    <td>${veic?.portador.saldoTotal}</td>

                </tr>
            </g:each>

            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${baseVeiculosCount}" params="${params}"/>

        <export:formats formats="['csv', 'excel', 'pdf']" params="${params}"/>
    </div>
</div>
</body>
</html>