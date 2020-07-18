<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Veículos Categoria ${categoriaInstance?.nome}</h3>
    </div>

    <div class="panel-body">
        %{--SEARCH--}%
        <div>
            <div class="row">
                <div class="form-group col-xs-4">
                    <label for="searchPlaca">Placa</label>
                    <input type="search" class="form-control placa" name="searchPlaca" id="searchPlaca"
                           value="${searchPlaca}">
                </div>

                <div class="form-group col-xs-4">
                    <label for="searchModelo">Modelo</label>
                    <input type="search" class="form-control uppercase" name="searchModelo" id="searchModelo"
                           value="${searchModelo}">
                </div>

            </div>

            <div style="float: right; margin-top: 1em; margin-bottom: 1em;">
                <button type="button" class="btn btn-default" onclick="pesquisarVeiculo()">
                    <i class="glyphicon glyphicon-search"></i>
                    Pesquisar
                </button>

                <button type="button" class="btn btn-default" onclick="limparVeiculos()">
                    <i class="glyphicon glyphicon-erase"></i>
                    Limpar
                </button>
            </div>
        </div>
    </div>
</div>

