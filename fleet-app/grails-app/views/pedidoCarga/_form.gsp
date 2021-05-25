<%@ page import="com.sysdata.gestaofrota.PedidoCargaProgramado; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Unidade; com.sysdata.gestaofrota.Rh; java.text.SimpleDateFormat; com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>


<g:hiddenField name="id" value="${pedidoCargaInstance?.id}"/>
<g:hiddenField name="action" value="${action}"/>
<g:hiddenField name="vinculoCartao" value="${pedidoCargaInstance?.unidade?.rh?.vinculoCartao}"/>
<g:hiddenField name="tipoPedido" value="${pedidoCargaInstance?.class?.simpleName}"/>


<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Empresa / Unidade</h3>
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="empresa">Empresa</label>
                <g:select name="empresa"
                          from="${Rh.ativosPrepago.list()}"
                          class="form-control"
                          value="${pedidoCargaInstance?.unidade?.rh?.id}"
                          optionKey="id"
                          optionValue="nome"
                          noSelection="['': '--Selecione uma Empresa--']"
                          dataAttrs="[vinculoCartao: 'vinculoCartao']"/>
            </div>
            <div class="col-xs-4 input-group-sm">
                <label class="control-label" for="unidade">Unidade</label>
                <g:select name="unidade"
                          from="${Unidade.findAllByRh(pedidoCargaInstance?.unidade?.rh)}"
                          class="form-control"
                          value="${pedidoCargaInstance?.unidade?.id}"
                            optionKey="id"
                          optionValue="nome"
                          noSelection="['': '--Selecione uma Unidade--']"/>
            </div>
        </div>
    </div>
</div>


<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3 class="panel-title">Dados Pedido</h3>
    </div>

    <div class="panel-body">
        <div class="row">

            <g:if test="${action == Util.ACTION_NEW}">
                <div class="col-md-3">
                    <g:checkBox name="pedidoProgramado" value="${pedidoCargaInstance?.instanceOf(PedidoCargaProgramado)}" /> Pedido Programado
                </div>

                <div class="col-md-3">
                    <g:radioGroup name="tipoTaxa" labels="['Taxa Administração', 'Taxa Desconto']" values="[1, 2]" value="1" >
                        <p>${it.radio} ${it.label}</p>
                    </g:radioGroup>
                </div>

                <div class="col-md-3">
                    <label class="control-label">${pedidoCargaInstance?.taxa ? 'Taxa Administração' : 'Taxa Desconto'}</label>
                    <p id="taxaPedido" class="form-control-static">${pedidoCargaInstance?.taxa ?: pedidoCargaInstance.taxaDesconto}%</p>
                </div>

            </g:if>


            <g:if test="${action != Util.ACTION_NEW && pedidoCargaInstance}">

                <div class="col-md-3">

                    <div class="panel panel-green">

                        <div class="panel-heading">
                            Total Pedido ${pedidoCargaInstance?.taxa ? '(+ taxa)' : '(- taxa)'}
                        </div>

                        <div class="panel-body">
                            <p class="form-control-static" style="font-size: large"><strong><g:formatNumber number="${pedidoCargaInstance?.total}" type="currency"/></strong> </p>
                            <p class="form-control-static"><strong>${pedidoCargaInstance?.taxa ? 'Taxa Administração' : 'Taxa Desconto'}</strong> ${pedidoCargaInstance?.taxa ?: pedidoCargaInstance.taxaDesconto}%</p>
                        </div>

                    </div>

                </div>
            </g:if>
        </div>

        <hr/>

        <div id="instancia"></div>

    </div>
</div>

<div id="programado"></div>

<g:if test="${action == Util.ACTION_NEW}">
    <g:render template="/categoriaFuncionario/list"
              model="${[categoriaFuncionarioInstanceList: pedidoCargaInstance?.perfisRecarga]}"/>

</g:if>


<g:if test="${! pedidoCargaInstance?.unidade || pedidoCargaInstance?.unidade?.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO}">
    <div id="pedidoFuncionarios" style="display: none">
        <g:render template="funcionarios" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
    </div>
</g:if>

<g:if test="${! pedidoCargaInstance?.unidade || pedidoCargaInstance?.unidade?.rh?.vinculoCartao == TipoVinculoCartao.MAQUINA}">
    <div id="pedidoVeiculos" style="display: none">
        <g:render template="veiculos" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>
    </div>
</g:if>



%{--<g:render template="taxasCartao" />--}%

<br/>


<asset:javascript src="util.js"></asset:javascript>

<script>
    var itensPedidos = [];
    var loc = $(location).attr('href');

    var relative = "";

    var pedId

    var taxas = {
        admin: 0,
        desc: 0
    }

/*
    if (loc.includes("\/create"))
        relative = "../";
    else if (loc.includes("\/show\/"))
        relative = "../";
*/


    var qtdeAgenda = 0;

    $(document).ready(function () {
        valorCategoria = $("input#valorCargaCategoria").val();

        pedId = $("input#id").val();


        var defaultForm = $("form#defaultForm");
        //desabilita o formulario de ser submetido caso o botão 'enter' seja pressionado
        defaultForm.keypress(function(event){
            if (event.keyCode === 10 || event.keyCode === 13)
                event.preventDefault();
        });

        //substitui os dados que serão enviado para o servidor
        defaultForm.submit(function (e) {
            waitingDialog.show();
        });

        carregarPedido();

        if (typeof pedId !== 'undefined' && pedId !== "") {

            var vinculoCartao = $("#vinculoCartao").val();
            if (vinculoCartao === 'Funcionário') {
                $("div#pedidoFuncionarios").show();
                $("div#pedidoVeiculos").hide();
            }
            else if (vinculoCartao === 'Máquina') {
                $("div#pedidoFuncionarios").hide();
                $("div#pedidoVeiculos").show();
            }
        }

        $("select[name=empresa]").change(function() {
            carregarTaxasRh($(this).val());
            carregarUnidades($(this).val());
        });

        $("select[name=unidade]").change(function() {
            carregarPerfisRecarga($(this).val());

            var vinculoCartao = $("select[name=empresa]").find(':selected').data('vinculocartao');

            if (vinculoCartao === 'Funcionário')
                carregarFuncionarios($(this).val());
            else if (vinculoCartao === 'Máquina') {
                carregarVeiculos($(this).val());
            }
        });

        $("input[name=tipoTaxa]").change(function() {
            mostrarTaxa($(this));
        });


        $("#pedidoProgramado").click(function(e) {

            var checked = $(this).is(":checked");

            var divInstancia = $("div#instancia");
            var divProgramado = $("div#programado");

            if (checked) {
                divInstancia.hide();
                divProgramado.show();

                //$.get(relative + "pedidoCarga/loadPedidoProgramado", {id: pedId}, function(data) {
                $.get("${createLink(action: 'loadPedidoProgramado')}", {id: pedId}, function(data) {
                    divProgramado.html(data);

                    //carregarAgenda();
                });

            } else
                carregarPedidoInstancia();

        });

        $("#btnAdd").click(function() {
            carregarAgenda();
        });

    });

//    function carregarAgenda() {
//        $.get(relative + "addNovaAgenda", {id: pedId, idx: qtdeAgenda++}, function(data) {
//            $("div#agenda").append(data);
//        });
//    }

    function carregarPedido() {

        const divInstancia = $("div#instancia");
        const divProgramado = $("div#programado");

        const tipoPedido = $("#tipoPedido").val();

        if (typeof tipoPedido !== 'undefined' && tipoPedido === 'PedidoCargaProgramado') {

            divInstancia.hide();
            divProgramado.show();

            $.get("${createLink(action: 'loadPedidoProgramado')}", {id: pedId}, function(data) {
                divProgramado.html(data);
            });

        } else {
            divInstancia.show();
            divProgramado.hide();

            $.get("${createLink(action: 'loadPedidoInstancia')}", {id: pedId}, function(data) {
                divInstancia.html(data);
                $('.datepicker').datepicker({language: 'pt-BR'});
            });
        }

    }

    function mostrarTaxa(elem) {
        var tipoTaxa = elem.val();

        var taxaElem = $("p#taxaPedido");
        var taxaLabel = taxaElem.parent().find('label');

        if (tipoTaxa === '1') {
            taxaLabel.html("Taxa Administração");
            taxaElem.html(taxas.admin);
        } else if (tipoTaxa === '2') {
            taxaLabel.html("Taxa Desconto");
            taxaElem.html(taxas.desc);
        }

    }


    function carregarVeiculos(unidId) {
        $("div#pedidoFuncionarios").hide();
        $("div#pedidoVeiculos").show();

        filtrarVeiculos(unidId);
    }

    function filtrarVeiculos(unidId) {

        var output = {
            id: $("input#id").val(),
            categoria: $('input[name=categoriaSelecionada]:checked').val(),
            actionView: $("input#action").val(),
            unidade: unidId
        };

        waitingDialog.show("Aguarde...");
        $.ajax({
            url: "listVeiculos",
            data: output,
            dataType: 'html',

            success: function (data) {
                $("div#veiculos-list").html(data);

                var submitButton = $("input#submitButton");
                if (submitButton.is(":disabled")) submitButton.attr('disabled', false);
            },

            complete: function () {
                onFuncionarioListLoadComplete();
            }
        });

    }

    function carregarFuncionarios(unidId) {

        $("div#pedidoFuncionarios").show();
        $("div#pedidoVeiculos").hide();

        filtrarFuncionarios(unidId);
    }

    function filtrarFuncionarios(unidId) {
        var output = {
            id: $("input#id").val(),
            categoria: $('input[name=categoriaSelecionada]:checked').val(),
            actionView: $("input#action").val(),
            unidade: unidId
        };

        waitingDialog.show("Aguarde...");
        $.ajax({
            url: "listFuncionarios",
            data: output,
            dataType: 'html',

            success: function (data) {
                $("div#funcionario-list").html(data);

                var submitButton = $("input#submitButton");
                if (submitButton.is(":disabled")) submitButton.attr('disabled', false);
            },

            complete: function () {
                onFuncionarioListLoadComplete();
            }
        });
    }



    function carregarPerfisRecarga(unidId) {

        $.getJSON("${createLink(controller:'categoriaFuncionario', action: 'getAllByUnidade')}", { unidId: unidId },
                function(data, status) {
                    $("#tabCateg > tbody").html("")

                    if (status === 'success') {
                        data.forEach(function(categ, idx, arr) {
                            var checked = "";

                            var vinculoCartao = $("select[name=empresa]").find(':selected').data('vinculocartao');
                            var changeListener = "";
                            if (vinculoCartao === 'Funcionário')
                                changeListener = "filtrarFuncionarios(" + unidId + ")";
                            else if (vinculoCartao === 'Máquina')
                                changeListener = "filtrarVeiculos(" + unidId + ")";


                            $("#tabCateg > tbody:last-child")
                                    .append("<tr><td><input type='radio' name='categoriaSelecionada' value='" + categ.id + "'"  +
                                    "onchange='" + changeListener + "' class='enable'" + checked + "/></td>" +
                                    "<td>" + categ.nome + "</td>" +
                                    "<td>" + categ.valorCarga + "</td></tr>");
                        })

                    }
                })

    }

    function carregarUnidades(rhId) {

        //$.getJSON(relative + "unidade/getAllByRh", { rhId: rhId },
        $.getJSON("${createLink(controller: 'unidade', action: 'getAllByRh')}", { rhId: rhId },
                function(data, status) {
                    var selUnid = $("select[name=unidade]")
                    selUnid.html("")
                    if (status === 'success') {
                        var unidList = []
                        unidList.push("<option value>--Selecione uma Unidade--</option>")
                        $.each(data, function(i, unid) {
                            unidList.push("<option value='" + unid.id + "'>" + unid.name + "</option>")
                        })
                        selUnid.append(unidList)
                    } else {
                        alert("Erro no Servidor (Código: " + status + ")")
                    }
                })
    }


    function carregarTaxasRh(rhId) {

        //$.getJSON(relative + "rh/getTaxas", { rhId: rhId },
        $.getJSON("${createLink(controller: 'rh', action: 'getTaxas')}", { rhId: rhId },
                function (data, status) {
                    if (status === 'success') {
                        taxas = data;
                        mostrarTaxa($("input[name=tipoTaxa]"));
                    }
                })
    }



    function limparFuncionario(){
        $("input#searchMatricula").val('');
        $("input#searchNome").val('');
        $("input#searchCpf").val('');

        pesquisarFuncionario();
    }

    function pesquisarFuncionario() {
        var categoriaSelecionada = $('input[name=categoriaSelecionada]:checked').val();
        if(typeof categoriaSelecionada == 'undefined'){
            alert("Seleciona primeiramente uma Categoria de Funcionário.")
            return;
        }

        var query = {
            id: $("input#id").val(),
            categoria: categoriaSelecionada,
            searchMatricula: $("input#searchMatricula").val(),
            searchNome: $("input#searchNome").val(),
            searchCpf: $("input#searchCpf").val(),
            actionView: $("input#action").val()
        };

        waitingDialog.show("Aguarde...");
        $.ajax({
            url: "filterFuncionarios",
            data: query,
            dataType: 'html',

            success: function (data) {
                $("div#funcionario-list").html(data);
            },
            complete: function () {
                onFuncionarioListLoadComplete();
            }
        });
    }

    function onFuncionarioListLoadComplete() {
        waitingDialog.hide();
        var action = $("input#action").val();
        if (action === "visualizando") {
            $("input[type='checkbox']").each(function (e) {
                $(this).attr("disabled", "true");
                $(this).attr("checked", "true");
            });
        }
        else if(action === "novo" || action === "editando") {
            //verifica se já há algum item dentro do array itens pedidos
            for (var i = 0; i < itensPedidos.length; i++) {
                var itemPedido = itensPedidos[i];
                $("input#valorCarga-" + itemPedido.id).val(itemPedido.valor);
                $("input[type='checkbox'][name='funcionariosAtivos'][value=" + itemPedido.id + "]").prop('checked', itemPedido.ativo);
            }

            $('.money').maskMoney({prefix: 'R$ ', decimal: ',', thousands: '.', affixesStay: false});
        }
    }

    function selecinaTodosFuncionarios() {

        var output = {
            id: $("input#id").val(),
            categoria: $('input[name=categoriaSelecionada]:checked').val()
        };

        if (typeof output.categoria == 'undefined') return;


        $.ajax({
            url: "getAllFuncionariosIds",
            data: output,
            dataType: 'json',

            success: function (data) {
                if (typeof data.idsFuncionarios !== 'undefined') {
                    var ativo = $("input[type='checkbox'][name='selectAll']").is(":checked");
                    $('div#funcionario-list .checkbox').prop('checked', ativo);

                    valorCategoria = data.valorCategoria;
                    data.idsFuncionarios.forEach(function (funcionarioId) {
                        setItemPedido(funcionarioId);
                    });
                }
            }
        });
    }

    function setItemPedido(itemId) {
        //formata valor da carga

        var elemValor = $("input#valorCarga_" + itemId);

        var valorItem = elemValor.val();

        var chkItem = $("input[type='checkbox'][name='func_" + itemId + "']");

        if ((typeof valorItem === 'undefined' || valorItem === '' || valorItem === 'R$ 0,00') && chkItem.is(":checked")) {

            $("div#msg_"+itemId).show();
            $("div#msg_"+itemId).html("Valor não pode ser nulo");

            $("input#valorCarga_" + itemId).focus();
            return;
        } else {

            var oldValue = elemValor.data('oldvalue');
            oldValue = parseCurrency(oldValue);
            valorItem = parseCurrency(valorItem);

            if (valorItem <= oldValue) {
                $("div#msg_"+itemId).hide();
                $("div#msg_"+itemId).html("");
                var itemPedido = {
                    id: itemId,
                    valor: valorItem,
                    ativo: chkItem.is(":checked")
                };

                var it = itensPedidos.filter(function (item) {
                    return item.id === itemPedido.id
                });

                if (Array.isArray(it) && it.length === 0)
                    itensPedidos.push(itemPedido);
                else
                    it = itemPedido;

            } else {
                $("div#msg_"+itemId).show();
                $("div#msg_"+itemId).html("Valor maior que perfil recarga");
                $("input#valorCarga_" + itemId).focus();
            }


        }

    }


    /*
     function carregarTaxasCartao() {
     waitingDialog.show("Aguarde...");

     var params={
     unidId:$("#unidade_id").val(),
     pedId:$("#id").val()
     };

     $.ajax({
     url: "loadTaxasCartao",
     data: params,
     dataType: 'html',

     success:function(data){
     $("div#taxasCartao").html(data);
     }
     });
     }
     */




</script>