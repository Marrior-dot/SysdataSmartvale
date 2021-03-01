<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes Lote de Pagamento</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes Lote de Pagamento</h4>
        </div>
        <div class="panel-body">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h5>Lote #${lotePagamento.id}</h5>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-4">
                            <label>Data</label>
                            <p><g:formatDate date="${lotePagamento.dateCreated}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação</label>
                            <p>${lotePagamento.status.nome}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação Emissão</label>
                            <p>${lotePagamento.statusEmissao.nome}</p>
                        </div>
                    </div>
                </div>
            </div>

            <table class="table">
                <thead>
                    <th>#</th>
                    <th>Conveniado</th>
                    <th>Valor</th>
                    <th>Data Prevista</th>
                    <th>Data Pagamento</th>
                    <th>Status</th>
                </thead>
                <tbody>
                    
                </tbody>
            </table>

        </div>
    </div>





</body>
</html>