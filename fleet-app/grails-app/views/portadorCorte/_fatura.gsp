<g:if test="${fatura}">
    <div class="panel panel-default">
        <div class="panel-heading">Fatura</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-3">
                    <label>Data Corte</label>
                    <p><g:formatDate date="${fatura.data}" format="dd/MM/yyyy"></g:formatDate></p>
                </div>
                <div class="col-md-3">
                    <label>Data Vencimento</label>
                    <p><g:formatDate date="${fatura.dataVencimento}" format="dd/MM/yyyy"></g:formatDate></p>
                </div>
            </div>

            <table class="table table-stripped">
                <thead>
                <th>Data</th>
                <th>Item</th>
                <th>Valor</th>
                </thead>
                <tbody>
                <g:each in="${fatura.itens}" var="fat">
                    <tr>
                        <td><g:formatDate date="${fat.data}" format="dd/MM/yyyy"></g:formatDate></td>
                        <td>${fat.descricao}</td>
                        <td><g:formatNumber number="${fat.valor}" type="currency"></g:formatNumber></td>
                    </tr>
                </g:each>
                </tbody>
                <tfoot>
                <td></td>
                <td><strong>Total Fatura</strong></td>
                <td><g:formatNumber number="${fatura.valorTotal}" type="currency"></g:formatNumber></td>
                </tfoot>
            </table>
        </div>
    </div>
</g:if>
<g:else>
    <div class="well text-center">NÃO HÁ FATURA</div>
</g:else>

