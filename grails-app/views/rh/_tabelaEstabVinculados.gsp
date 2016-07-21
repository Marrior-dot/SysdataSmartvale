<table id="estTable" class="table table-striped table-bordered table-hover table-condensed table-default">

    <thead>
    <th>CNPJ</th>
    <th>Razão Social</th>
    <th>Nome Fantasia</th>
    <th></th>
    </thead>
    <tbody>
    <g:each in="${estabelecimentoInstanceList}" status="i" var="estab">
        <tr>
            <td>${estab.cnpj}</td>
            <td>${estab.nome}</td>
            <td>${estab.nomeFantasia}</td>
            <td>
                <a class="btn" id="remover${estab.id}" name="remover">
                     <span class="glyphicon glyphicon-trash" style="color: red" aria-hidden="true" ></span>
                </a>
            </td>
        </tr>
    </g:each>
    </tbody>

</table>