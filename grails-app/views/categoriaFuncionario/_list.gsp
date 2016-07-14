<br/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Categorias de Funcionários</h3>
    </div>

    <div class="panel-body">
        <g:if test="${categoriaFuncionarioInstanceList?.size() > 0}">
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th></th>
                    <th>Código</th>
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
                        <td>${categoria?.rh?.codigo}</td>
                        <td>${categoria?.nome}</td>
                        <td>${categoria?.valorCarga}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="well well-lg text-center"><b>SEM DADOS</b></div>
        </g:else>
    </div>
</div>