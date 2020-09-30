<%@ page import="com.sysdata.gestaofrota.Estabelecimento" %>
<%@ page import="com.sysdata.gestaofrota.Participante" %>
<%@ page import="com.sysdata.gestaofrota.Empresa" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Base de Estabelecimentos"/>
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

                <div class="panel-body" >

                <div class="row">
                    <div class="col-md-3">
                        <label class="control-label" for="cnpj">CNPJ</label>
                        <g:textField name="cnpj" class="form-control cnpj" value="${params.cnpj}"></g:textField>
                    </div>
                   <!-- <div class="col-md-3">
                        <label class="control-label" for="dataInicio">Data Inicial</label>
                        <g:textField name="dataInicio" class="form-control datepicker" value="${params.dataInicio}"></g:textField>
                    </div>
                    <div class="col-md-3">
                        <label class="control-label" for="dataFim">Data Final</label>
                        <g:textField name="dataFim" class="form-control datepicker" value="${params.dataFim}"></g:textField>
                    </div> -->
                </div>
                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>

        <div class="table-responsive">

        <table class="table table-bordered table-stripped">
            <thead>
            <th>Cod.Estab</th>
            <th>CNPJ</th>
            <th>Razão Social</th>
            <th>Fantasia</th>
            <th>Endereço</th>
            <th>Cidade - UF</th>
            <th>Telefone</th>
            <th>Email</th>
            <th>Reembolso</th>


            </thead>
            <tbody>
            <g:each in="${baseEstabelecimentosList}" var="est">
                <tr>
                    <td>${est.codigo}</td>
                    <td>${est?.cnpj}</td>
                    <td>${est.nome}</td>
                    <td>${est?.nomeFantasia}</td>
                    <td>${est?.endereco?.logradouro}</td>
                    <td>${est?.endereco?.cidade}</td>
                    <td>${est?.telefone}</td>
                    <td>${est?.email}</td>
                    <td>${est?.empresa?.taxaReembolso}</td>

                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${baseEstabelecimentosList}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>


</body>
</html>