<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <title>Configuração de Propriedades Dinâmicas</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Configuração de Propriedades Dinâmicas</h4>
        </div>
        <div class="panel-body">

            <g:link uri="/" class="btn btn-default">
                <span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/>
            </g:link>
            <g:link action="create" class="btn btn-default">
                <span class="glyphicon glyphicon-plus"></span>&nbsp;Nova Configuração
            </g:link>

            <br/>
            <br/>

            <table class="table table-bordered table-stripped">
                <thead>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Tipo Participante</th>
                    <th>Ativo</th>
                </thead>
                <tbody>
                    <g:each in="${configList}" var="cfg">
                        <tr>
                            <td><g:link action="show" id="${cfg.id}">${cfg.id}</g:link></td>
                            <td>${cfg.nome}</td>
                            <td>${cfg.tipoParticipante}</td>
                            <td>${cfg.ativo ? 'Sim' : 'Não'}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>