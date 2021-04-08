<table id="tableFunc" class="table">
    <thead>
    <th>CPF</th>
    <th>Nome</th>
    <th>Matrícula</th>
    <th>Cartão</th>
    </thead>
    <tbody>
    <g:each in="${funcionariosList}" var="func">
        <tr data-id="${func.id}">
            <td><g:link controller="funcionario" action="show" id="${func.id}">${func.cpf}</g:link></td>
            <td>${func.nome}</td>
            <td>${func.matricula}</td>
            <td>${func.portador?.cartaoAtivo}</td>
        </tr>
    </g:each>
    </tbody>
</table>
<g:paginate total="${funcionariosCount}" params="${params}"></g:paginate>
