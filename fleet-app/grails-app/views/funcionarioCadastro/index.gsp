<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Listagem de Funcionários</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Listagem de Funcionários</h4>
        </div>
        <div class="panel-body">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Filtro
                </div>
                <g:form action="index">
                <div class="panel-body">
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>CPF</label>
                            <g:textField name="cpf" class="form-control cpf" value="${params.cpf}"></g:textField>
                        </div>
                        <div class="col-md-4">
                            <label>Nome</label>
                            <g:textField name="nome" class="form-control" value="${params.nome}"></g:textField>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-md-3">
                            <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-search"></i> Pesquisar</button>
                        </div>
                    </div>
                </div>
                </g:form>
            </div>
            <table class="table">
                <thead>
                    <th>CPF</th>
                    <th>Nome</th>
                    <th>Cliente</th>
                    <th>Unidade</th>
                </thead>
                <tbody>
                    <g:each in="${funcionarioList}" var="func">
                        <tr>
                            <td><g:link controller="funcionario" action="show" id="${func.id}">${func.cpf}</g:link></td>
                            <td>${func.nome}</td>
                            <td>${func.unidade.rh.nome}</td>
                            <td>${func.unidade.nome}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <g:paginate total="${funcionarioCount}" params="${params}"></g:paginate>
        </div>
    </div>
</body>
</html>