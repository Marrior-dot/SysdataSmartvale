<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<div class="panel-top">
    <g:if test="${pedidoCargaInstanceList?.size() > 0}">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr>
                <th>#</th>
                <th>Unidade</th>
                <th>Data Pedido</th>
                <th>Data Carga</th>
                <th>Total</th>
                <th>Status</th>
                <th class="text-center">Ações</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${pedidoCargaInstanceList}" var="pedido">
                <tr>
                    <td>
                        <g:link controller="pedidoCarga" action="show" id="${pedido.id}">
                            ${pedido.id}
                        </g:link>
                    </td>
                    <td>${pedido?.unidade?.nome}</td>
                    <td><g:formatDate date="${pedido?.dateCreated}" format="dd/MM/yyyy"/></td>
                    <td><g:formatDate date="${pedido?.dataCarga}" format="dd/MM/yyyy"/></td>
                    <td><g:formatNumber number="${pedido?.total}" type="currency"/></td>
                    <td>${pedido.status.nome}</td>
                    <td class="text-center">
                        <sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADMIN">

                            <g:if test="${pedido.status == StatusPedidoCarga.COBRANCA}">

                                <g:link class="btn btn-primary" action="liberarPedido" id="${pedido.id}" title="Liberar" onClick="return confirm('Confirma a liberação do Pedido?')">
                                    <i class="glyphicon glyphicon-share"></i>
                                </g:link>
                            </g:if>

                            <g:if test="${pedido.status in [StatusPedidoCarga.NOVO, StatusPedidoCarga.AGENDADO]}">
                                <g:link class="btn btn-danger" action="cancelarPedido" id="${pedido.id}" title="Cancelar" onClick="return confirm('Confirma o cancelamento do Pedido?')">
                                    <i class="glyphicon glyphicon-remove"></i>
                                </g:link>
                            </g:if>
                        </sec:ifAnyGranted>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <div>
            <g:paginate class="pagination" total="${pedidoCargaInstanceCount ?: 0}"/>
        </div>

    </g:if>
    <g:else>
        <div class="well text-center">SEM DADOS</div>
    </g:else>

</div>

