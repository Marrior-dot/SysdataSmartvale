<!DOCTYPE html>

<html>
<head>
    <meta name="layout" content="layout-restrito">
    <title>Lista de Portadores</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Lista de Portadores</h4>
        </div>
        <div class="panel-body">

            <div class="panel panel-default">
                <div class="panel-heading">
                    Pesquisa
                </div>
                <div class="panel-body">
                    <g:render template="/components/rhUnidadeSelect"></g:render>
                </div>
            </div>

            <div class="panel-footer">
                <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
            </div>


            <table class="table">
            <thead>
                <th>Nome</th>
                <th>Cliente</th>
                <th>Unidade</th>
            </thead>
            <tbody>
                <g:each in="${portadoresList}" var="portador">
                    <tr>
                        <td><g:link action="show" id="${portador.id}">${portador.nomeEmbossing}</g:link></td>
                        <td>${portador.unidade?.rh?.nome}</td>
                        <td>${portador.unidade?.nome}</td>
                    </tr>
                </g:each>
            </tbody>
            </table>
            <g:paginate total="${portadoresCount}"/>
        </div>
    </div>
</body>
</html>