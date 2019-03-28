<div class="panel panel-default">
    <div class="panel-heading">
        <h4>Pesquisa</h4>
    </div>
    <div class="panel-body">
        <g:form name="filtro" action="${action}">
            <div class="row">
                <div class="col-md-4">
                    <label for="dataInicial">Período</label>

                    <div class="input-group">
                        <input type="text" name="dataInicial" id="dataInicial" class="form-control datepicker"
                               value="${dataInicial?.format('dd/MM/yyyy')}">
                        <span class="input-group-addon" id="basic-addon1">até</span>
                        <input type="text" name="dataFinal" id="dataFinal" class="form-control datepicker"
                               value="${dataFinal?.format('dd/MM/yyyy')}">
                    </div>
                </div>

                <div class="form-group col-md-4">
                    <label for="numeroCartao">Cartão</label>
                    <input type="text" class="form-control cartao" name="numeroCartao" id="numeroCartao" value="${numeroCartao}">
                </div>

                <div class="form-group col-md-2">
                    <label for="codigoEstabelecimento">Cod Estabelecimento</label>
                    <input type="text" class="form-control" name="codigoEstabelecimento" id="codigoEstabelecimento" value="${codigoEstabelecimento}">
                </div>

                <div class="form-group col-md-2">
                    <label for="nsu">NSU</label>
                    <input type="number" class="form-control" name="nsu" id="nsu" value="${nsu}">
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-2">
                    <label for="tipo">Tipo</label>
                    <g:select name="tipo" class="form-control" from="${tipos}" value="${tipo}"
                              optionValue="nome" noSelection="['': 'Todos']"/>
                </div>
            </div>
        </g:form>
    </div>

    <div class="panel-footer text-right">
        <button type="submit" class="btn btn-default" form="filtro">
            <i class="glyphicon glyphicon-search"></i>
            Pesquisar
        </button>
    </div>
</div>