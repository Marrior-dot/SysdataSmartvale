<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes de Template Mail</title>

    <style>
        table  {
            table-layout: fixed;
            word-wrap: break-word;
        }
        table th {
            width: 20%;
        }
    </style>

</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes de Template Mail</h4>
        </div>
        <div class="panel-body">
            <div class="buttons-top">
                <g:link action="index" class="btn btn-default"><i class="glyphicon glyphicon-list"></i> Listagem de Templates Email</g:link>
            </div>

            <alert:all/>

            <table class="table table-bordered">
                <tr>
                    <th>ID</th>
                    <td>${mailTemplate.key}</td>
                </tr>
                <tr>
                    <th>Nome</th>
                    <td>${mailTemplate.name}</td>
                </tr>
                <tr>
                    <th>Assunto</th>
                    <td>${mailTemplate.subject}</td>
                </tr>
                <tr>
                    <th>Remetente</th>
                    <td>${mailTemplate.from}</td>
                </tr>
                <tr>
                    <th>Destinatário(s)</th>
                    <td>${mailTemplate.to}</td>
                </tr>
                <tr>
                    <th>Cópia(s) Para</th>
                    <td>${mailTemplate.ccTo}</td>
                </tr>
                <tr>
                    <th>Corpo Email</th>
                    <td>${mailTemplate.body}</td>
                </tr>
            </table>
        </div>
        <div class="panel-footer">
            <g:form>
                <g:hiddenField name="id" value="${mailTemplate.id}"></g:hiddenField>
                <button class="btn btn-default" name="_action_edit"><i class="glyphicon glyphicon-edit"></i> Editar</button>
                <button class="btn btn-danger" name="_action_delete" onclick="return confirm('Confirma a exclusão do Template Email?')">
                    <i class="glyphicon glyphicon-trash"></i> Excluir</button>
            </g:form>
        </div>
    </div>
</body>
</html>