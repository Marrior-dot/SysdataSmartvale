<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito"/>
    <title>Mock Transações</title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>Gerador de Transações</h4>
    </div>
    <div class="panel-body">

        <alert:all/>

        <g:form action="gerarTransacoes">
            <div class="row">
                <div class="form-group col-md-3">
                    <label for="quantidade">Quantidade</label>
                    <g:textField name="quantidade" class="form-control"></g:textField>
                </div>
                <div class="form-group col-md-3">
                    <label for="dataInicio">Data Inicial</label>
                    <g:textField name="dataInicio" class="form-control datepicker"></g:textField>
                </div>
                <div class="form-group col-md-3">
                    <label for="dataFim">Data Final</label>
                    <g:textField name="dataFim" class="form-control datepicker"></g:textField>
                </div>
            </div>

            <div class="row">
                <div class="col-md-3">
                    <button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-plus"></span>&nbsp;Gerar</button>
                </div>
            </div>

        </g:form>

        <div class="panel-top">
            <g:if test="${transacaoCount}">
                <table class="table table-bordered">
                    <thead>
                    <th></th>
                    </thead>
                    <tbody>
                    <g:each in="${transacaoList}" var="tr">
                        <tr>
                            <td>${tr.id}</td>
                            <td>${tr.dataHora.format('dd/MM/yyyy')}</td>
                            <td>${tr.numeroCartao}</td>
                            <td>${tr.codigoEstabelecimento}</td>
                            <td>${tr.valor}</td>
                        </tr>
                    </g:each>
                    <g:paginate total="${transacaoCount}"></g:paginate>
                    </tbody>

                </table>
            </g:if>
            <g:else>
                <div class="well text-center">Sem Dados</div>
            </g:else>
        </div>
    </div>
</div>
</body>
</html>