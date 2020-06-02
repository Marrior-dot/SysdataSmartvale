<%@ page import="com.sysdata.gestaofrota.Util" %>
<g:if test="${transacaoInstanceTotal > 0}">
    <table class="table table-striped table-bordered table-condensed" style="font-size: 12px">
        <thead>
        <th>ID</th>
        <th><g:message code="transacao.nsu.label" default="NSU"/></th>
        <th>Data/Hora</th>
        <th><g:message code="transacao.codigoEstabelecimento.label" default="Codigo Estabelecimento"/></th>
        <th><g:message code="transacao.cartao.label" default="Cartao"/></th>
        <th><g:message code="transacao.participante.label" default="Funcionário"/></th>
        <th><g:message code="transacao.tipo.label" default="Tipo"/></th>
        <th><g:message code="transacao.status.label" default="Status"/></th>
        <th><g:message code="transacao.statusControle.label" default="Status Controle"/></th>
        <th><g:message code="transacao.valor.label" default="Valor"/></th>
        <sec:ifAnyGranted roles="ROLE_PROC">
            <th class="text-center">Ações</th>
        </sec:ifAnyGranted>
        </thead>

        <tbody>
        <g:each in="${transacaoInstanceList}" var="transacaoInstance">
            <tr>
                <td>
                    <g:link action="show" id="${transacaoInstance.id}">
                        ${fieldValue(bean: transacaoInstance, field: "id")}
                    </g:link>
                </td>
                <td>${transacaoInstance.nsu}</td>
                <td>
                    <g:formatDate date="${transacaoInstance.dateCreated}" format="dd/MM/yyyy HH:mm:ss"/>
                </td>
                <td>${transacaoInstance.codigoEstabelecimento}</td>
                <td>${transacaoInstance.cartao ? transacaoInstance.cartao.numeroMascarado : '---'}</td>
                <td>${transacaoInstance.participante?.nome}</td>
                <td>${transacaoInstance?.tipo?.nome}</td>
                <td>${transacaoInstance.status?.nome}</td>
                <td>${transacaoInstance.statusControle?.nome}</td>
                <td><g:formatNumber number="${transacaoInstance.valor}" format="#0.00"/></td>

                <sec:ifAnyGranted roles="ROLE_PROC">
                    <td class="text-center">
                        <g:if test="${transacaoInstance.estornavel}">
                            <a class="btn btn-default btn-sm" title="Estornar" href="${createLink(action: 'estornar', id: transacaoInstance.id)}">
                                <i class="glyphicon glyphicon-remove-circle"></i>
                            </a>
                        </g:if>
                    </td>
                </sec:ifAnyGranted>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="paginateButtons">
        <g:paginate total="${transacaoInstanceTotal}" params="${params}"/>
    </div>
</g:if>
<g:else>
    <div class="well well-lg text-center"><strong>SEM TRANSAÇÕES</strong></div>
</g:else>

