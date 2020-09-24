<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Base de Funcionarios"/>
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

                    <g:render template="/components/rhUnidadeSelect"></g:render>

                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>



        <table class="table table-bordered table-stripped">
            <thead>
            <th>Matricula</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Empresa</th>
            <th>Unidade</th>
            <th>CNH Categoria</th>
            <th>CNH Valid</th>
            <th>Veiculos Qtd</th>
            </thead>
            <tbody>
            <g:each in="${baseFuncionariosList}" var="func">
                <tr>
                    <td>${func[0]}</td>
                    <td>${func[1]}</td>
                    <td>${func[2]}</td>
                    <td>${func[3]}</td>
                    <td>${func[4]}</td>
                    <td>${func[5]}</td>
                    <td><g:formatDate date="${func[6]}" format="dd/MM/yy"/></td>
                    <td>${func[7]}</td>

                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>

        <g:paginate total="${baseFuncionariosCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>


</body>
</html>