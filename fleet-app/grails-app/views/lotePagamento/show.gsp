<%@ page import="com.sysdata.gestaofrota.StatusLotePagamento; com.sysdata.gestaofrota.StatusEmissao" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes Lote de Pagamento</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes Lote de Pagamento</h4>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span> Voltar</g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-heading">
                    <h5>Lote #${lotePagamento.id}</h5>
                </div>
                <div class="panel-body">
                    <alert:all/>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Data</label>
                            <p><g:formatDate date="${lotePagamento.dateCreated}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação</label>
                            <p>${lotePagamento.status.nome}</p>
                        </div>
                        <div class="col-md-4">
                            <label>Situação Emissão</label>
                            <p>${lotePagamento.statusEmissao.nome}</p>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Valor Total</label>
                            <h4>
                                <span class="label label-default"><g:formatNumber number="${lotePagamento.total}" type="currency"></g:formatNumber></span>
                            </h4>
                        </div>
                        <div class="col-md-4">
                            <label>Status Retorno</label>
                            <p>${lotePagamento.statusRetorno}</p>
                        </div>

                    </div>
                </div>
                <sec:ifAnyGranted roles="ROLE_PROC, ROLE_PROC_FINANC">
                    <g:if test="${lotePagamento.statusEmissao == StatusEmissao.NAO_GERAR && lotePagamento.status == StatusLotePagamento.FECHADO}">
                        <div class="panel-footer">
                            <g:form>
                                <g:hiddenField name="id" value="${lotePagamento.id}"></g:hiddenField>
                                <button type="submit"
                                        name="_action_confirm"
                                        class="btn btn-success"
                                        onclick="return confirm('Confirma o Lote para Envio?')">
                                    <i class="fa fa-thumbs-o-up fa-fw"></i> Confirmar p/ Envio</button>

                                <button type="submit"
                                        name="_action_cancel"
                                        class="btn btn-danger"
                                        onclick="return confirm('Confirma o Cancelamento do Lote?')">
                                    <i class="fa fa-close fa-fw"></i> Cancelar Lote</button>


                                <sec:ifAnyGranted roles="ROLE_PROC">
                                    <button type="submit"
                                            name="_action_redo"
                                            class="btn btn-default"
                                            onclick="return confirm('Confirma refazimento do Lote Recebimento?')">
                                        Refazer Lote Recebimento</button>

                                </sec:ifAnyGranted>


                            </g:form>
                        </div>
                    </g:if>
                </sec:ifAnyGranted>


            </div>

            <table class="table">
                <thead>
                    <th>Conveniado</th>
                    <th>Valor</th>
                    <th>Data Prevista</th>
                    <th>Data Pagamento</th>
                    <th>Status</th>
                </thead>
                <tbody>
                <g:each in="${lotePagamento.pagamentos.sort{it.id}}" var="pagto">
                    <tr>
                        <td><g:link action="showPagamentoLote" id="${pagto.id}">${pagto.estabelecimento.identificacaoResumida}</g:link></td>
                        <td><g:formatNumber number="${pagto.valor}" type="currency"></g:formatNumber></td>
                        <td><g:formatDate date="${pagto.dataPrevista}" format="dd/MM/yy"></g:formatDate></td>
                        <td><g:formatDate date="${pagto.dataPagamento}" format="dd/MM/yy"></g:formatDate></td>
                        <td>${pagto.status.nome}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>