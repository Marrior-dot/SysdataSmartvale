<%@ page import="com.sysdata.gestaofrota.StatusLotePagamento; com.sysdata.gestaofrota.StatusEmissao" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes Lote de Recebimentos</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes Lote de Recebimentos</h4>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span> Voltar</g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-heading">
                    <h5>Lote #${loteRecebimento.id}</h5>
                </div>
                <div class="panel-body">
                    <alert:all/>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Data</label>
                            <p><g:formatDate date="${loteRecebimento.dateCreated}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação</label>
                            <p>${loteRecebimento.status.nome}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação Emissão</label>
                            <p>${loteRecebimento.statusEmissao.nome}</p>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Valor Bruto</label>
                            <h4>
                                <span class="label label-default"><g:formatNumber number="${loteRecebimento.totalBruto}" type="currency"></g:formatNumber></span>
                            </h4>
                        </div>
                        <div class="col-md-4">
                            <label>Valor Comissão</label>
                            <h4>
                                <span class="label label-default"><g:formatNumber number="${loteRecebimento.totalComissao}" type="currency"></g:formatNumber></span>
                            </h4>
                        </div>
                        <div class="col-md-4">
                            <label>Valor Líquido</label>
                            <h4>
                                <span class="label label-default"><g:formatNumber number="${loteRecebimento.totalLiquido}" type="currency"></g:formatNumber></span>
                            </h4>
                        </div>
                    </div>
                </div>
            </div>

            <table class="table">
                <thead>
                    <th>Cliente</th>
                    <th>Valor Bruto</th>
                    <th>Valor Comissão</th>
                    <th>Valor Líquido</th>
                    <th>Data Prevista</th>
                    <th>Data Recebimento</th>
                    <th>Status</th>
                </thead>
                <tbody>
                <g:each in="${loteRecebimento.recebimentos.sort{it.id}}" var="receb">
                    <tr>
                        <td>${receb.convenio.identificacaoResumida}</td>
                        <td><g:formatNumber number="${receb.valorBruto}" type="currency"></g:formatNumber></td>
                        <td><g:formatNumber number="${receb.valorTaxaAdm}" type="currency"></g:formatNumber></td>
                        <td><g:formatNumber number="${receb.valor}" type="currency"></g:formatNumber></td>
                        <td><g:formatDate date="${receb.dataPrevista}" format="dd/MM/yy"></g:formatDate></td>
                        <td><g:formatDate date="${receb.dataRecebimento}" format="dd/MM/yy"></g:formatDate></td>
                        <td>${receb.status.nome}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>