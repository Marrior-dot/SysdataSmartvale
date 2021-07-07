<%@ page import="com.sysdata.gestaofrota.StatusLotePagamento; com.sysdata.gestaofrota.StatusEmissao" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes Pagamento Lote</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes Pagamento Lote</h4>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link action="show" id="${pagamentoLote.lotePagamento.id}" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span> Voltar</g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-heading">
                    <h5>Pagamento Lote #${pagamentoLote.id}</h5>
                </div>
                <div class="panel-body">
                    <alert:all/>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Estabelecimento</label>
                            <p><g:link controller="postoCombustivel" action="show" id="${pagamentoLote.estabelecimento.id}">${pagamentoLote.estabelecimento.identificacaoResumida}</g:link></p>
                        </div>
                        <div class="col-md-4">
                            <label>Status</label>
                            <p>${pagamentoLote.status.nome}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Status Retorno</label>
                            <p>${pagamentoLote.statusRetorno}</p>
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Banco</label>
                            <p>${pagamentoLote.dadoBancario.banco.nome}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Agência</label>
                            <p>${pagamentoLote.dadoBancario.agencia}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Conta</label>
                            <p>${pagamentoLote.dadoBancario.conta}</p>
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Data Prevista</label>
                            <p><g:formatDate date="${pagamentoLote.dataPrevista}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Data Pagamento</label>
                            <p><g:formatDate date="${pagamentoLote.dataPagamento}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Valor</label>
                            <p><g:formatNumber number="${pagamentoLote.valor}" type="currency"></g:formatNumber></p>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <g:form>
                        <g:hiddenField name="id" value="${pagamentoLote.id}"></g:hiddenField>
                        <button type="submit" name="_action_updateDataBank" class="btn btn-default"><i class="fa fa-refresh fa-fw"></i> Atualizar Domicílio Bancário</button>
                    </g:form>
                </div>
            </div>

            <table class="table">
                <thead>
                    <th>Data Prevista</th>
                    <th>Data Pagamento</th>
                    <th>Valor</th>
                    <th>Valor Bruto</th>
                    <th>Taxa Adm</th>
                    <th>Status</th>
                </thead>
                <tbody>
                <g:each in="${pagamentoLote.pagamentos}" var="pagto">
                    <tr>
                        <td><g:link action="showPagamentoEstab" params="[pagEstabId: pagto.id, pagLoteId: pagamentoLote.id]"><g:formatDate date="${pagto.dataProgramada}" format="dd/MM/yy"></g:formatDate></g:link></td>
                        <td><g:formatDate date="${pagto.dataEfetivada}" format="dd/MM/yy"></g:formatDate></td>
                        <td><g:formatNumber number="${pagto.valor}" type="currency"></g:formatNumber></td>
                        <td><g:formatNumber number="${pagto.valorBruto}" type="currency"></g:formatNumber></td>
                        <td>${pagto.taxaAdm}%</td>
                        <td>${pagto.status.nome}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>