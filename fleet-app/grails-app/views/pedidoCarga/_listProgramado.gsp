<div class="panel-top">
    <g:if test="${programadoList}">
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>Empresa</th>
                <th>Unidade</th>
                <th>Status</th>
                <th class="text-center">Ações</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${programadoList}" var="pr">
                <tr>
                    <td><g:link action="show" id="${pr.id}">${pr.id}</g:link></td>
                    <td>${pr.unidade.rh.nome}</td>
                    <td>${pr.unidade.nome}</td>
                    <td>${pr.statusProgramacao.nome}</td>
                    <td></td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <g:paginate class="pagination" total="${programadoCount ?: 0}"/>

    </g:if>
    <g:else>
        <div class="well text-center">SEM DADOS</div>
    </g:else>
</div>




