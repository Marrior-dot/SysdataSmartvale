<table class="table">
    <thead>
    <th>Data Prevista</th>
    <th>Valor Repasse</th>
    <th>Cartão</th>
    <th>Data Transação</th>
    <th>Valor Bruto</th>
    </thead>
    <tbody>
        <g:each in="${entriesList}">
            <tr>
                <td><g:formatDate date="${it.dataPrevista}" format="dd/MM/yy"></g:formatDate></td>
                <td><g:formatNumber number="${it.valor}" type="currency"></g:formatNumber></td>
                <td>${it.transacao.cartao.numeroMascarado}</td>
                <td><g:formatDate date="${it.transacao.dataHora}" format="dd/MM/yy"></g:formatDate></td>
                <td><g:formatNumber number="${it.transacao.valor}" type="currency"></g:formatNumber></td>
            </tr>
        </g:each>
    </tbody>
</table>
<g:paginate total="${entriesCount}"></g:paginate>