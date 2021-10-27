<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Lista de Cortes de Repasses</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h3>Lista de Cortes de Repasses</h3>
        </div>
        <div class="panel-body">
            <alert:all/>
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
                        <th>Data Criação</th>
                        <th>Data Prevista</th>
                        <th>Data Fechamento</th>
                        <th>Data Cobrança</th>
                        <th>Status</th>
                        <th>Tipo Corte</th>
                        <th>Ações</th>
                        </thead>
                        <tbody>
                        <g:each in="${corteList}" var="corte">
                            <tr>
                                <td>${corte.id}</td>
                                <td><g:formatDate date="${corte.dateCreated}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td><g:formatDate date="${corte.dataPrevista}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td><g:formatDate date="${corte.dataFechamento}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td><g:formatDate date="${corte.dataCobranca}" format="dd/MM/yyyy"></g:formatDate></td>
                                <td>${corte.status.nome}</td>
                                <td>${corte.tipoCorte.nome}</td>
                                <td><g:link action="undo" id="${corte.id}" onclick="return confirm('Confirma o desfaturamento do corte?')"><i class="glyphicon glyphicon-step-backward"/></g:link></td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:paginate total="${corteCount}" params="${params}"></g:paginate>
                </div>
            </div>
        </div>
    </div>
</body>
</html>