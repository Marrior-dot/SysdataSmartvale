<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Listagem de Templates Email</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Listagem de Templates Email</h4>
        </div>
            <div class="panel-body">
                <div class="buttons-top">
                    <g:link action="create" class="btn btn-default">
                        <i class="glyphicon glyphicon-plus"></i> Novo Template</g:link>
                </div>

                <alert:all/>

            <g:if test="${mailTemplateList}">
                <table class="table">
                    <thead>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Assunto</th>
                    </thead>
                    <tbody>
                        <g:each in="${mailTemplateList}" var="template">
                            <tr>
                                <td><g:link action="show" id="${template.id}">${template.key}</g:link></td>
                                <td>${template.name}</td>
                                <td>${template.subject}</td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
                <g:paginate total="${mailTemplateCount}" params="${params}"></g:paginate>
            </g:if>
            <g:else>
                <div class="well text-center">NÃO RETORNOU DADOS</div>
            </g:else>
        </div>
    </div>
</body>
</html>