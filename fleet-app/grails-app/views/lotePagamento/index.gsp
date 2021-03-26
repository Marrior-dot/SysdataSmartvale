<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Listagem de Lotes de Pagamento à Conveniados</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h3>Listagem de Lotes de Pagamentos à Conveniados</h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link uri="/" class="btn btn-default"><span class="glyphicon glyphicon-home"></span></g:link>
                    <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-list"></span></g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-body">
                    <table class="table">
                        <thead>
                        <th>#</th>
                        <th>Data Criação</th>
                        <th>Data Prevista</th>
                        <th>Total</th>
                        </thead>
                        <tbody>
                        <g:each in="${lotePagamentoList}" var="lote">
                            <tr>
                                <td><g:link action="show" id="${lote.id}">${lote.id}</g:link></td>
                                <td><g:formatDate date="${lote.dateCreated}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td><g:formatDate date="${lote.dataEfetivacao}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td><g:formatNumber number="${lote.total}" type="currency"></g:formatNumber></td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:paginate total="${lotePagamentoCount}" params="${params}"></g:paginate>
                </div>
            </div>
        </div>
    </div>
</body>
</html>