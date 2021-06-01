<div class="panel panel-default">
    <div class="panel-heading">
        Faturas de Portadores
    </div>
    <div class="panel-body">
        <table class="table">
            <thead>
                <th>Portador</th>
                <th>Total</th>
            </thead>
            <tbody>
                <g:each in="${faturasPortadoresList}" var="faturaPortador">
                    <tr>
                        <td><g:link controller="portadorCorte" action="showPortador" params="[prtId: faturaPortador.conta.portador.id]">${faturaPortador.conta.portador.nomeEmbossing}</g:link></td>
                        <td><g:formatNumber number="${faturaPortador.valorTotal}" type="currency"></g:formatNumber></td>
                    </tr>
                </g:each>
            </tbody>
        </table>
%{--        <g:paginate total="faturasPortadoresCount"></g:paginate>--}%
    </div>
</div>
