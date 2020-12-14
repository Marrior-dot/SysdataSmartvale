<g:if test="${lotesEmbossingList}">

    <div class="panel panel-default panel-top">
        <div class="panel-body">
            <table class="table">
                <thead>
                <th>#</th>
                <th>Data Criação</th>
                <th>Liberado por</th>
                <th>Status</th>
                </thead>
                <tbody>
                <g:each in="${lotesEmbossingList}" var="lotEmb">
                    <tr>
                        <td>${lotEmb.id}</td>
                        <td><g:formatDate date="${lotEmb.dateCreated}" format="dd/MM/yy"></g:formatDate></td>
                        <td>${lotEmb.usuario.name}</td>
                        <td>${lotEmb.status.nome}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <g:paginate total="${lotesEmbossingCount}"></g:paginate>
        </div>
    </div>



</g:if>
<g:else>
    <div class="panel panel-default panel-top">
        <div class="panel-body">
            <div class="alert alert-info text-center" role="alert">Não há Lotes Embossing gerados</div>
        </div>
    </div>
</g:else>