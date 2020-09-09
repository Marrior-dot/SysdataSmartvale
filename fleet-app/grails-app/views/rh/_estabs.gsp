<%@ page import="com.sysdata.gestaofrota.Estado" %>
<script type="application/javascript">

    var state = {};

    var updateState = function () {
        setState();
    }

    /**
    *   Controla o estado (state) das páginas contendo os ECs marcados/desmarcados:
     *   Considera como verdade o que está armazenado na variável 'state' e não os itens da tabela.
     *   Os itens da tabela servem apenas para preenchimento de 'state' quando um offset ou um EC não estão mapeados.
     *   Na verdade, a variável controla o estado das páginas visitadas e não a verdade absoluta dos ECs marcados ou não;
     *   o que será devidamente considerado na persistência no servidor
     */

    function setState() {

        var estabTable = $("table#estabTable");
        var offset = estabTable.data('offset');

        var matchPage = state.pages.filter(function(pg) {
                                                return pg.offset == offset;
                                            });

        if (matchPage.length === 0) {
            matchPage = {};
            matchPage.offset = offset;
            matchPage.estabList = [];
            state.pages.push(matchPage);
        } else
            matchPage = matchPage[0];

        // Itera pelas linhas da tabela que representam os ECs vindos
        estabTable.find("tbody > tr").each(function(){

            $(this).find("td > :checkbox").each(function(){

                var eid = $(this).attr("id").split('_')[1];
                var value = $(this).is(":checked");

                var estab = matchPage.estabList.filter(function(est) {
                                                            return est.id == eid;
                                                        });
                if (estab.length === 0) {
                    estab = {};
                    estab.id = eid;
                    matchPage.estabList.push(estab);
                } else
                    estab = estab[0];
                estab.checked = value;
            });
        });
    }

    $(function () {

        initState();
        carregarEstadosEstabelecimentos();
        carregarEstabsVinculados();

        // Pega evento de click nos links de paginação
        $("#divEstabs").on('click', 'a', function(){
            event.preventDefault();

            var link = $(this).attr("href");
            var offsetIndex = link.lastIndexOf("offset");
            var aux = link.substring(offsetIndex, link.length);
            var offsetParam = aux.substring(0, aux.indexOf("&") > -1 ? aux.indexOf("&") : aux.length);
            var offset = offsetParam.split("=")[1];

            if (state.action === "show")
                carregarEstabsVinculados(offset);
            else if (state.action === "edit")
                carregarEstabsEdicao(offset);
        });

        // Disparado na seleção de um novo UF
        $("select[name=estados]").change(function() {
            carregarCidadesEstabelecimentos($(this).val());
        });

        // Disparado na seleção de uma nova Cidade
        $("#divCidades").on('change', 'select[name=cidades]', function() {
            if (state.action === "show")
                carregarEstabsVinculados();
            else if (state.action === "edit")
                carregarEstabsEdicao();

        })


        // Filtra ECs por Nome Fantasia
        $("input[name=fantasia]").keyup(function() {

            if ($(this).val().length % 2 == 0) {
                if (state.action === "show")
                    carregarEstabsVinculados();
                else if (state.action === "edit")
                    carregarEstabsEdicao();
            }
        });


        $("input[name=fantasia]").change(function() {

        });


        // Edita para marcar/desmarcar ECs
        $("button[name=editEstabs]").click(function() {
            carregarEstabsEdicao();

            initState("edit");

            $(this).hide();
            $("button[name=saveEstabs]").show();

        });


        // Salva vinculando/desvinculando ECs ao RH
        $("button[name=saveEstabs]").click(function() {

            if (state.pages.length > 0) {

                var sent = confirm("Confirma a vinculação/desvinculação dos ECs ao Cliente?");
                if (sent) {
                    var rhId = $("#rhId").val();

                    var estToSave = []
                    state.pages.forEach(function (page) {
                        var sendEstabs = page.estabList.map(function (est) {
                                                                return {id: est.id, checked: est.checked}

                                                        });
                        estToSave = estToSave.concat(sendEstabs);
                    });

                    $.ajax({
                        url: "${createLink(controller:'rh', action:'saveEstabsVinculados')}",
                        type: "POST",
                        data: JSON.stringify({rhId: rhId, ecs: estToSave}),
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            document.getElementById("divEstabs").innerHTML = data;
                        },
                        complete: function () {
                            $("button[name=saveEstabs]").hide();
                            $("button[name=editEstabs]").show();
                            carregarEstabsVinculados();
                        }
                    });

                }
            } else {
                alert("Nenhum EC foi marcado ou desmarcado, portanto nada será salvo!");

                $("button[name=saveEstabs]").hide();
                $("button[name=editEstabs]").show();
                carregarEstabsVinculados();
            }
            initState("show");
        });

        function initState(action) {
            state.action = typeof action !== 'undefined' ? action : 'show';
            state.pages = [];
        }

        // Na inicialização, carrega somente UFs que possuem ECs
        function carregarEstadosEstabelecimentos() {

            $.getJSON("${createLink(controller: 'estado', action: 'listAllByEstabelecimento')}",

                function(estabs) {
                    var estabSelect = $("select[name=estados]");
                    var options = "";
                    estabs.forEach(function(est) {
                        options += "<option value='" +est.id+ "'>" + est.nome + "</option>"
                    });
                    estabSelect.html(options);
                    estabSelect.selectpicker();
                }
            );
        }

        // Carrega Cidades relacionadas ao UF selecionado e carrega ECs por UF
        function carregarCidadesEstabelecimentos(arrIds) {

            $.get("${createLink(controller: 'cidade', action: 'listAllByEstabelecimento')}",
                    {ufs: arrIds}
            )
            .done(function(gsp) {
                $("div#divCidades").html(gsp);
                $("select[name=cidades]").selectpicker();
            })
            .always(function() {
                if (state.action === "show")
                    carregarEstabsVinculados();
                else if (state.action === "edit")
                    carregarEstabsEdicao();
            });
        }

        // Carrega ECs vinculados por UFs && Cidades && Nome Fantasia
        function carregarEstabsVinculados(offset){

            var nome = $("input[name=fantasia]").val();
            var ufsIds = $("select[name=estados]").val();
            var cidIds = $("select[name=cidades]").val();
            var rhId = $("#rhId").val();

            $.ajax({
                url: "${g.createLink(controller:'rh', action:'listEstabsVinculados')}",
                data: { rhId: rhId, fantasia: nome, ufs: ufsIds, cids: cidIds, offset: offset},
                dataType: 'html',
                success: function (data) {
                    document.getElementById("divEstabs").innerHTML = data;
                }
            });
        }

        /**
        *   Sincroniza os ECs carregados na páginação com os ECs armazenados no 'state'
         *   para refletir as marcações/demarcações realizadas.
         *   Percorre a lista de tr>td, encontra o ID do EC no nome do elem checkbox,
         *   encontra na lista de ECs da página selecionada (offset) e seta o estado do checkbox
         */

        function synchPage(offset) {

            if (state.action === 'edit') {
                var page = state.pages.filter(function(pg) {
                                                    return pg.offset == offset
                                                });
                if (page.length > 0) {
                    page = page[0];

                    var estabTable = $("table#estabTable");
                    estabTable.find("tbody > tr").each(function(){

                        $(this).find("td > :checkbox").each(function(){

                            var eid = $(this).attr("id").split('_')[1];

                            var estab = page.estabList.filter(function(est) {
                                                            return est.id == eid
                                                        });
                            if (estab.length > 0) {
                                estab = estab[0];
                                $(this).attr('checked', estab.checked);
                            }
                        })
                    });
                }
            }
        }

        function carregarEstabsEdicao(offset){

            var rhId = $("#rhId").val();
            var nome = $("input[name=fantasia]").val();
            var ufsIds = $("select[name=estados]").val();
            var cidIds = $("select[name=cidades]").val();

            $.ajax({
                url: "${g.createLink(controller:'rh', action:'editEstabsVinculados')}",
                data: { rhId: rhId, fantasia: nome, ufs: ufsIds, cids: cidIds, offset: offset},
                dataType: 'html',
                success: function (data) {
                    document.getElementById("divEstabs").innerHTML = data;
                },
                complete: function() {
                    synchPage(offset);
                }
            });
        }
    });
</script>

<g:hiddenField name="rhId" value="${rhInstance?.id}"/>
<div id="mensagem"></div>

<div class="panel">
    <div class="panel panel-default">

        <div class="panel-heading">
            Filtros
        </div>

        <div class="panel-body">
            <div class="row">
                <div class="col-md-6">
                    <label for="estados">Estado</label>
                    <select name="estados" class="form-control selectpicker enable" multiple>
                    </select>
                </div>

                <div class="col-md-6">
                    <div id="divCidades">
                        <g:render template="/cidade/cidadesSelect"></g:render>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <label for="fantasia">Nome Fantasia</label>
                    <g:textField name="fantasia" class="form-control enable" ></g:textField>
                </div>
            </div>

        </div>

    </div>

    <g:form name="estabForm">
        <div id="divEstabs"></div>

        <div class="panel-footer">
            <button type="button" name="editEstabs" class="btn btn-default"><i class="glyphicon glyphicon-edit"></i>&nbsp;Editar</button>
            <button type="button" name="saveEstabs" class="btn btn-success" style="display: none;"><i class="glyphicon glyphicon-save"></i>&nbsp;Salvar</button>
        </div>
    </g:form>

</div>



