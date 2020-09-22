<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Base de Veículos"/>
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

        <table class="table table-bordered table-stripped">
            <thead>
            <th>Placa</th>
            <th>Marca/Modelo</th>
            <th>Empresa</th>
            <th>Unidade</th>
            <th>Ano Fabricação</th>
            <th>Hodômetro</th>
            </thead>
            <tbody>
            <g:each in="${baseVeiculosList}" var="veic">
                <tr>
                    <td>${veic[0]}</td>
                    <td>${veic[1]} / ${veic[2]}</td>
                    <td>${veic[3]}</td>
                    <td>${veic[4]}</td>
                    <td>${veic[5]}</td>
                    <td>${veic[6] ?: 0}</td>
                </tr>
            </g:each>
            </tbody>
            <tfoot>

            </tfoot>
        </table>

        <g:paginate total="${baseVeiculosCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>
</body>
</html>