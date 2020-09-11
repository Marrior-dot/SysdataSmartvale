<div class="panel panel-default">

    <div class="panel-heading">
        <g:if test="${action == 'show'}">
            Estabelecimentos Vinculados
        </g:if>
        <g:else>
            Vincular/Desvincular Estabelecimentos
        </g:else>
    </div>

    <div class="panel-body">
        <g:if test="${action == 'edit'}">
            <div class="row">
                <div class="col-md-6">
                    <g:checkBox name="selectAll" value="${params.selectAll}"/> <strong>Selecionar Todos da Página</strong>
                </div>
            </div>
        </g:if>

        <div class="row" style="margin-top: 1em;">
            <div class="col-md-12">
                <table id="estabTable" class="table table-stripped">
                    <thead>
                    <g:if test="${action == 'edit'}">
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
                                <td><g:checkBox name="est_${est.id}"
                                                value="${rhInstance.empresas.find { it == est } ? true : false}"
                                                onclick="updateState()"/></td>
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

                <g:paginate total="${estabCount}" params="${params}"></g:paginate>

            </div>
        </div>

    </div>


</div>

