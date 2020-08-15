<g:if test="${faturasList}">
    <div class="panel panel-default">
    <div class="panel-heading">Faturas</div>
    <div class="panel-body">
        <table class="table table-stripped">
        <thead>
            <th>#</th>
            <th>Data Corte</th>
            <th>Data Vencimento</th>
            <th>Status</th>
            <th>Valor Total</th>
        </thead>
        <tbody>
            <g:each in="${faturasList}" var="fat">
                <tr>
                    <th><g:link controller="fatura" action="show" id="${fat.id}">${fat.id}</g:link></th>
                    <th><g:formatDate date="${fat.data}" format="dd/MM/yy"></g:formatDate></th>
                    <th><g:formatDate date="${fat.dataVencimento}" format="dd/MM/yy"></g:formatDate></th>
                    <th>${fat.status.nome}</th>
                    <th><g:formatNumber number="${fat.valorTotal}" type="currency"></g:formatNumber></th>
                </tr>
            </g:each>
        </tbody>


        </table>
    </div>
</div>


</g:if>
<g:else>
    <div class="well text-center">NÃO HÁ FATURAS PARA ESTE PORTADOR</div>
</g:else>
