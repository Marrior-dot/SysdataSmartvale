<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Demonstrativo de Desempenho"/>
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

                    <div class="row">
                        <div class="col-md-3">
                            <label class="control-label" for="placa">Placa</label>
                            <g:textField name="placa" class="form-control placa" value="${params.placa}"></g:textField>
                        </div>
                        <!--<div class="col-md-3">
                            <label class="control-label" for="dataInicio">Data Inicial</label>
                            <g:textField name="dataInicio" class="form-control datepicker" value="${params.dataInicio}"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label class="control-label" for="dataFim">Data Final</label>
                            <g:textField name="dataFim" class="form-control datepicker" value="${params.dataFim}"></g:textField>
                        </div>-->
                    </div>
                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>

        <table class="table table-bordered table-stripped">
            <thead>
            <th>Placa</th>
            <th>Marca/Modelo</th>
            <th>Funcionário</th>
            <th>Empresa</th>
            <th>Unidade</th>
            <th>Km Rodados</th>
            <th>Lts Abastecidos</th>
            <th>Desempenho (km/l)</th>
            </thead>

            <tbody>
            <g:each in="${desempenhoList}" var="csm">
                <tr>
                    <td>${csm[0]}</td>
                    <td>${csm[1]} / ${csm[2]}</td>
                    <td>(${csm[3]}) ${csm[4]}</td>
                    <td>${csm[5]}</td>
                    <td>${csm[6]}</td>
                    <td>${csm[7]}</td>
                    <td>${csm[8]}</td>
                    <td>${csm[9].round(2)}</td>
                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>

        <g:paginate total="${desempenhoCount}" />

       <export:formats formats="['csv', 'excel', 'pdf']" params="${params}"/>
    </div>
</div>
</body>
</html>