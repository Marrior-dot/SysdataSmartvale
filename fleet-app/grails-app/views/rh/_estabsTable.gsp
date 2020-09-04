<div class="panel panel-default">

    <div class="panel-heading">
        <g:if test="${action == 'show'}">
            Estabelecimentos Vinculados
        </g:if>
        <g:else>
            Vincular Estabelecimentos

            <g:set var="rhService" bean="rhService"/>
        </g:else>
    </div>

    <div class="panel-body">
        <g:if test="${action == 'edit'}">
            <g:checkBox name="selectAll" value="${params.selectAll}" />
        </g:if>

        <table class="table table-stripped">
            <thead>
            <g:if test="${action == 'add'}">
                <th></th>
            </g:if>
            <th>CNPJ</th>
            <th>Nome Fantasia</th>
            <th>Endereço</th>
            <th>Cidade</th>
            <th>UF</th>
            </thead>
            <tbody>
            <g:each in="${estabList}" var="est">
                <tr>
                    <g:if test="${action == 'edit'}">
                        <td><g:checkBox name="est_${est.id}" value="${rhInstance.empresas.find { it == est }}" /></td>
                    </g:if>
                    <td>${est.cnpj}</td>
                    <td>${est.nomeFantasia}</td>
                    <td>${est.endereco.logradouro}</td>
                    <td>${est.endereco.cidade.nome}</td>
                    <td>${est.endereco.cidade.estado.uf}</td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <g:paginate total="${estabCount}"></g:paginate>
    </div>

</div>

