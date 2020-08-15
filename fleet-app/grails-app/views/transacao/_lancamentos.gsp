<%@ page import="com.sysdata.gestaofrota.Portador" %>
<table class="table table-stripped">
<thead>
    <th>#</th>
    <th>Data Efetivação</th>
    <th>Conta</th>
    <th>Tipo</th>
    <th>Status</th>
    <th>Valor</th>
</thead>
<tbody>
    <g:each in="${transacao.lancamentos.sort{it.tipo}}" var="lcto">
        <tr>
            <td>${lcto.id}</td>
            <td><g:formatDate date="${lcto.dataEfetivacao}" format="dd/MM/yyyy"></g:formatDate></td>
            <td><g:link controller="portadorCorte" action="showPortador" params="[prtId: Portador.findByConta(lcto.conta)?.id]"> ${lcto.conta.id}</g:link></td>
            <td>${lcto.tipo?.nome}</td>
            <td>${lcto.status?.nome}</td>
            <td><g:formatNumber number="${lcto.valor}" type="currency"></g:formatNumber></td>
        </tr>
    </g:each>
</tbody>

</table>