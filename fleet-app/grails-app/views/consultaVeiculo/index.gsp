<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Consulta Veiculo"/>
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
            <th>Data</th>
            <th>Hora</th>
            <th>Estabelecimento</th>
            <th>Placa</th>
            <th>Cartão / Vinculado </th>
            <th>Ult. Hodometro (Kms)</th>
            <th>Produto</th>
            <th>Preço Litro</th>
            <th>Valor</th>
            <th>Cliente</th>
            <th>Unidade</th>

            </thead>
            <tbody>

            <g:each in="${consultaVeiculoList}" var="cveic">
                <tr>
                        <td><g:formatDate date="${cveic?.dateCreated}" format="dd/MM/yyyy" /></td>
                        <td><g:formatDate date="${cveic?.dateCreated}" format="HH:mm:ss" /></td>
                        <td>${cveic?.estabelecimento?.nomeFantasia}</td>
                        <td>${cveic?.placa}</td>
                        <td>${cveic?.cartao?.numero}</td>
                        <td>${cveic?.maquina?.hodometro}</td>
                        <td>${cveic?.produtos?.produto?.nome}</td>
                        <td>${cveic?.precoUnitario}</td>
                        <td>${cveic?.valor}</td>
                        <td>${cveic?.cartao?.portador?.unidade?.rh?.nomeFantasia}</td>
                        <td>${cveic?.cartao?.portador?.unidade?.nome}</td>
                </tr>
            </g:each>

            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${consultaVeiculoCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>
</body>
</html>