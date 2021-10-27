<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Lista de Lotes de Recebimentos de Clientes</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h3>Lista de Lotes de Recebimentos de Clientes</h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link uri="/" class="btn btn-default"><span class="glyphicon glyphicon-home"></span> Início</g:link>
                    <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-list"></span> Lista de Lotes</g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-body">
                    <table class="table">
                        <thead>
                        <th>Data</th>
                        <th>Total Bruto</th>
                        <th>Total Comissão</th>
                        <th>Total Líquido</th>
                        <th>Status</th>
                        <th>Envio</th>
                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <th>Ação</th>
                        </sec:ifAnyGranted>
                        </thead>
                        <tbody>
                        <g:each in="${loteRecebimentoList}" var="lote">
                            <tr>
                                <td><g:link action="show" id="${lote.id}"><g:formatDate date="${lote.dateCreated}" format="dd/MM/yyyy"></g:formatDate></g:link></td>
                                <td><g:formatNumber number="${lote.totalBruto}" type="currency"></g:formatNumber></td>
                                <td><g:formatNumber number="${lote.totalComissao}" type="currency"></g:formatNumber></td>
                                <td><g:formatNumber number="${lote.totalLiquido}" type="currency"></g:formatNumber></td>
                                <td>${lote.status.nome}</td>
                                <td>${lote.statusEmissao.nome}</td>
                                <sec:ifAnyGranted roles="ROLE_PROC">
                                    <td><g:link action="undo" id="${lote.id}" onclick="return confirm('Confirma a exclusão do Lote?')"><i class="glyphicon glyphicon-step-backward"/></g:link></td>
                                </sec:ifAnyGranted>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:paginate total="${loteRecebimentoCount}" params="${params}"></g:paginate>
                </div>
            </div>
        </div>
    </div>
</body>
</html>