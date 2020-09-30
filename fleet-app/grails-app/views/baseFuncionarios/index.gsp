<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.Cartao" %>

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

        <div class="table-responsive">

        <table class="table table-bordered table-stripped table-responsive ">
            <thead>
            <th>Matrícula</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Cartão</th>
            <th>Empresa</th>
            <th>Unidade</th>
            <th>CNH</th>
            <th>CNH Categoria</th>
            <th>CNH Validade</th>
            <th>Email</th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>Cidade-UF</th>

            </thead>
            <tbody>
            <g:each in="${baseFuncionariosList}" var="func">
                <tr>
                    <td>${func.matricula}</td>
                    <td>${func.nome}</td>
                    <td>${func.cpf}</td>
                    <td>${func?.portador?.cartoes?.numero}</td>

                    <td>${func.unidade.rh.nomeFantasia}</td>
                    <td>${func.unidade.nome}</td>
                    <td>${func.cnh}</td>
                    <td>${func.categoriaCnh.nome}</td>
                    <td><g:formatDate date="${func.validadeCnh}" format="dd/MM/yy"/></td>
                    <td>${func?.email}</td>
                    <td>${func?.telefone}</td>
                    <td>${func?.portador?.endereco?.logradouro}</td>
                    <td>${func?.portador?.endereco?.cidade}</td>
                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${baseFuncionariosCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>


</body>
</html>