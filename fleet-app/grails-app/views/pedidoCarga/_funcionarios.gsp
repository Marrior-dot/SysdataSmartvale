<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Funcionários Categoria ${categoriaInstance?.nome}</h3>
    </div>

    <div class="panel-body">
        %{--SEARCH--}%
        <div>
            <div class="row">
                <div class="form-group col-xs-4">
                    <label for="searchMatricula">Matrícula</label>
                    <input type="search" class="form-control matricula" name="searchMatricula" id="searchMatricula"
                           value="${searchMatricula}">
                </div>

                <div class="form-group col-xs-4">
                    <label for="searchNome">Nome</label>
                    <input type="search" class="form-control uppercase" name="searchNome" id="searchNome"
                           value="${searchNome}">
                </div>

                <div class="form-group col-xs-4">
                    <label for="searchCpf">CPF</label>
                    <input type="search" class="form-control cpf" name="searchCpf" id="searchCpf" value="${searchCpf}">
                </div>
            </div>

            <div style="float: right; margin-top: 1em; margin-bottom: 1em;">
                <button type="button" class="btn btn-default" onclick="pesquisarFuncionario()">
                    <i class="glyphicon glyphicon-search"></i>
                    Pesquisar
                </button>

                <button type="button" class="btn btn-default" onclick="limparFuncionario()">
                    <i class="glyphicon glyphicon-erase"></i>
                    Limpar
                </button>
            </div>
        </div>
        %{--SEARCH--}%

%{--
        <div class="checkbox">
            <label>
                <input type="checkbox" name="selectAll" class="checkbox" checked
                       onclick="selecinaTodosFuncionarios()"> Seleciona Todos Funcionários
            </label>
        </div>
--}%

        <div class="panel-top">
            <div id="funcionario-list">
                <g:render template="funcionarioList"  model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
            </div>
        </div>
    </div>
</div>

