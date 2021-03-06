<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Reembolsos Faturados"/>
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
                    <div class="row">
                        <div class="col-md-3">
                            <label class="control-label" for="cnpj">CNPJ</label>
                            <g:textField name="cnpj" class="form-control cnpj" value="${params.cnpj}"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label class="control-label" for="dataInicio">Data Inicial</label>
                            <g:textField name="dataInicio" class="form-control datepicker" value="${params.dataInicio}"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label class="control-label" for="dataFim">Data Final</label>
                            <g:textField name="dataFim" class="form-control datepicker" value="${params.dataFim}"></g:textField>
                        </div>
                    </div>
                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>

        <div class="table-scrolled">
            <table class="table table-bordered table-stripped">
                <thead>
                <th>Razão Social</th>
                <th>Nome Fantasia</th>
                <th>CNPJ</th>
                <th>Data Programada</th>
                <th>Valor Bruto</th>
                <th>Valor Reembolsar</th>
                <th>Taxa Adm (%)</th>
                <th>Taxa Adm (R$)</th>
                <th>Situação</th>
                <!--<th>Banco</th>
                <th>Agência</th>
                <th>Conta</th>
                <th>Nome Titular</th>
                <th>Doc Titular</th>-->
                </thead>
                <tbody>
                <g:each in="${reembolsoList}" var="proj">
                    <tr>
                        <td>${proj[0]}</td>
                        <td>${proj[1]}</td>
                        <td>${proj[2]}</td>
                        <td><g:formatDate date="${proj[3]}" format="dd/MM/yy"/></td>
                        <td><g:formatNumber number="${proj[4]}" type="currency"></g:formatNumber> </td>
                        <td><g:formatNumber number="${proj[5]}" type="currency"></g:formatNumber> </td>
                        <td>${proj[6]}</td>
                        <td><g:formatNumber number="${proj[7]}" type="currency"></g:formatNumber></td>
                        <td>${proj[8]?.nome}</td>
                        <!--<td>${proj[9]}</td>
                        <td>${proj[10]}</td>
                        <td>${proj[11]}</td>
                        <td>${proj[12]}</td>
                        <td>${proj[13]}</td>--!>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <g:paginate total="${reembolsoCount}" params="${params}"/>
        </div>
        <export:formats formats="['csv', 'excel', 'pdf']" params="${params}"/>
    </div>
</div>
</body>
</html>