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

            <div style="float: right; margin-top: 1em">
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

        <br/><br/>
        <div class="checkbox">
            <label>
                <input type="checkbox" name="selectAll" class="checkbox" checked
                       onclick="selecinaTodosFuncionarios()"> Seleciona Todos Funcionários
            </label>
        </div>
        <div id="funcionario-list">
            %{--<g:render template="funcionarioList"--}%
                      %{--model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>--}%
        </div>
    </div>
</div>

%{--
<script type="text/javascript">

    /*
     Faz o controle da quantidade de funcionarios selecionados
     e a compara com a quantidade total para marcar ou desmarcar a seleção de todos (checkbox)
     */
    function validateSelectAll(catId, check) {

        var count = countCat[catId];
        var total = totalCat[catId];

        count = check ? (count + 1) : (count - 1);

        countCat[catId] = count;

        $("input[name='selectAll']").attr("checked", (total == count));
    }

    function synchServer(funcId, check) {

        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'pedidoCarga',action:'synchServer') }",
            data: "funcId=" + funcId + "&check=" + check,
            success: function (o) {
                if (o.type == "error")
                    showError(o.message);
            },
            statusCode: {
                404: function () {
                    showError("Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });
    }

    function synchCheckAll(categ, check) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'pedidoCarga',action:'synchCheckAll')}",
            data: ((categ != null) ? 'categ=' + categ : "categ=all&unidId=${unidadeInstance?.id}") + "&check=" + check,
            success: function (o) {
                if (o.type == "error")
                    showError(o.message);
            },
            statusCode: {
                404: function () {
                    showError("Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        })
    }


    YAHOO.util.Event.onDOMReady(function () {

        var firstTime = true;

        /* No carregamento da tabela de funcionários por categoria:
         * 	1. Registra o total de funcionários por categoria;
         *	2. Verifica o checkbox de seleção de todos por categoria
         *	Obs: (workaround) Na primeira vez que este evento é capturado a seleção da primeira categoria ainda não foi realizada,
         * 	por isso o tratamento com a variável firstTime
         */

        GRAILSUI.funcSearchDT.subscribe("initEvent", function (args) {

            if (!firstTime) {
                var rec = GRAILSUI.funcSearchDT.getRecordSet().getRecords()[0];

                if (totalCat[rec.getData('categoria')] == undefined)
                    totalCat[rec.getData('categoria')] = GRAILSUI.funcSearchDT.configs.paginator._configs.totalRecords.value;

                if (countCat[rec.getData('categoria')] == undefined)
                    countCat[rec.getData('categoria')] = GRAILSUI.funcSearchDT.configs.paginator._configs.totalRecords.value;

                $("input[name='selectAll']").attr("checked", (countCat[rec.getData('categoria')] == totalCat[rec.getData('categoria')]));

            } else
                firstTime = false;
        });


        GRAILSUI.funcSearchDT.subscribe("dataReturnEvent", function (args) {

            var check = $("input[name='selectAll']").checked ? true : false;

            var recs = GRAILSUI.funcSearchDT.getRecordSet().getRecords();
            var i = 0;

            while (i < recs.length) {

//if(recs[i]!=undefined && recs[i].getData('selecao')!=check)
                recs[i].setData('selecao', check);
                i++;
            }

            GRAILSUI.funcSearchDT.render();

        });


        GRAILSUI.funcSearchDT.subscribe("rowSelectEvent", function (args) {
            var selected = GRAILSUI.funcSearchDT.getRecord(GRAILSUI.funcSearchDT.getSelectedRows()[0]);
            $("#selectedId").val(selected.getData('id'));
        });

        /*
         *	Pega REGISTRO e COLUNA do DT a partir do html element (checkbox)
         *	Seta o valor do registro agora com o que foi efetivamente informado
         *	Desmarca o check de Seleçao de Todos
         *
         */

        GRAILSUI.funcSearchDT.subscribe("checkboxClickEvent", function (args) {

            var elCheck = args.target;
            var newValue = elCheck.checked;
            var rec = this.getRecord(elCheck);
            var col = this.getColumn(elCheck)

            rec.setData(col.key, newValue);

            synchServer(rec.getData('id'), newValue);

            <%--			if(newValue)--%>
            <%--				totalPedido+=parseFloat(rec.getData('valorCarga'));--%>

            validateSelectAll(rec.getData('categoria'), newValue);

        });
    });


    var funcSel = [];


</script>
--}%
