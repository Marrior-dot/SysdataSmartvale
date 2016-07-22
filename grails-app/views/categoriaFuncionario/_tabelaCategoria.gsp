<table id="categoriaTable">
    <thead>
    <th>Nome</th>
    <th>Valor da Carga </th>
    <th>Ações Disponíveis</th>
    </thead>
    <tbody id="bodyTable">
    <g:each in="${categorias}" status="i" var="categoria">
        <tr>
            <g:hiddenField name="categoriaId" value="${categoria.id}"></g:hiddenField>
            <td><div class="catNome">${categoria.nome}</div></td>
            <td><div class="catValor">R$ ${categoria.valorCarga}</div></td>
            <td>
                <button class="btn btn-primary editarButton">Editar</button>
                <button class="btn btn-danger excluirButton">Excluir</button>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>