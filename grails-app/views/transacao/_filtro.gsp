<div class="panel panel-default">
    <div class="panel-heading">
        <h4>Pesquisa</h4>
    </div>
    <div class="panel-body">
        <g:form action="${action}">
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

                <div class="form-group col-md-3">
                    <label for="numeroCartao">Cartão</label>
                    <input type="text" class="form-control cartao" name="numeroCartao" id="numeroCartao" value="${numeroCartao}">
                </div>

                <div class="form-group col-md-2">
                    <label for="codigoEstabelecimento">Cod Estabelecimento</label>
                    <input type="text" class="form-control" name="codigoEstabelecimento" id="codigoEstabelecimento" value="${codigoEstabelecimento}">
                </div>

                <div class="col-md-3">
                    <label for="nsu">NSU</label>
                    <div class="input-group">
                        <input type="number" class="form-control" name="nsu" id="nsu" value="${nsu}">
                        <span class="input-group-btn">
                            <button type="submit" class="btn btn-default">Pesquisar</button>
                        </span>
                    </div>
                </div>
            </div>
        </g:form>
    </div>
</div>