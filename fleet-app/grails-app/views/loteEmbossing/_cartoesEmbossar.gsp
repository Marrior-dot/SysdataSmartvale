<g:if test="${cartoesEmbossarList}">
    <div class="panel panel-default panel-top">
        <div class="panel-body">
            <table class="table">
                <thead>
                <th>Cartão</th>
                <th>Portador</th>
                <th>Cliente</th>
                <th>Unidade</th>
                <th>Data Criação</th>
                <th>Tipo</th>
                </thead>
                <tbody>
                <g:each in="${cartoesEmbossarList}" var="crt">
                    <tr>
                        <td><g:link controller="cartao" action="show" id="${crt.id}">${crt.numeroMascarado}</g:link>    </td>
                        <td>${crt.portador.nomeEmbossing}</td>
                        <td>${crt.portador.unidade?.rh?.nome}</td>
                        <td>${crt.portador.unidade?.nome}</td>
                        <td><g:formatDate date="${crt.dateCreated}" format="dd/MM/yy"></g:formatDate></td>
                        <td>${crt.tipo.name}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <g:paginate total="${cartoesEmbossarCount}" params="${params}"></g:paginate>

        </div>
        <div class="panel-footer">

            <a href="${createLink(action: 'createLoteEmbosing')}"
               class="btn btn-success"
                onclick="return confirm('Confirma a Liberação dos Cartões para Embossing?')">
                <span class="glyphicon glyphicon-check"></span>&nbsp;Liberar p/ Embossing</a>

        </div>
    </div>
</g:if>
<g:else>
    <div class="panel panel-default panel-top">
        <div class="panel-body">
            <div class="row">
                <div class="alert alert-info text-center" role="alert">Não há Cartões a Embossar</div>
            </div>
        </div>
    </div>
</g:else>



