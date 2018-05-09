<g:if test="${fechamentoList?.size() > 0}">
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th class="text-center">Dia do Corte</th>
            <th class="text-center">Dias até o Vencimento</th>
            <th class="text-center">Opções</th>
        </tr>

        </thead>
        <tbody>
        <g:each in="${fechamentoList}" var="fechamento">
            <tr>
                <td class="text-center">${fechamento.diaCorte}</td>
                <td class="text-center">${fechamento.diasAteVencimento}</td>
                <td class="text-center">
                    <a type="button" class="btn btn-sm btn-success" title="Cortes" onclick="abrirCortes('${fechamento.id}')">
                        <i class="glyphicon glyphicon-th-list"></i>
                    </a>
                    <a type="button" class="btn btn-sm btn-danger" title="Excluir" onclick="removerFechamento('${fechamento.id}')">
                        <i class="glyphicon glyphicon-remove"></i>
                    </a>

                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</g:if>
<g:else>
    <div class="well text-center"><strong>SEM DADOS</strong></div>
</g:else>