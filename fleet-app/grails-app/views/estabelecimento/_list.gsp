<table class="table table-striped">
    <thead>
    <th>Cod.Estab</th>
    <th>Razão Social</th>
    <th>Nome Fantasia</th>
    </thead>
    <tbody>
        <g:each in="${}" var="est">
            <td><g:link action="show" id="${est.id}">${est.codigo}</g:link></td>
            <td>${est.nome}</td>
            <td>${est.nomeFantasia}</td>
        </g:each>
    </tbody>
</table>
<g:paginate total="${}" params="${params}"></g:paginate>