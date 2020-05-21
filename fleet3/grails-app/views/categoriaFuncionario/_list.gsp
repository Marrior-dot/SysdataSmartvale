<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Perfis de Recarga</h3>
    </div>

    <div class="panel-body">
        <table id="tabCateg" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th></th>
                <th>Nome</th>
                <th>Valor Carga</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${categoriaFuncionarioInstanceList}" var="categoria">
                <tr>
                    <td>
                        <input type="radio" name="categoriaSelecionada" value="${categoria.id}" onchange="selecionaCategoria()" class="enable"/>
                    </td>
                    <td>${categoria?.nome}</td>
                    <td>${categoria?.valorCarga}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
</div>
</div>