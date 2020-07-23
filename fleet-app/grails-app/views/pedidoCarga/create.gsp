<%--
  Created by IntelliJ IDEA.
  User: hyago
  Date: 08/07/16
  Time: 17:27
--%>

<%@ page import="com.sysdata.gestaofrota.Util" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

        <meta name="layout" content="layout-restrito"/>
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}"/>
        <title><g:message code="default.new.label" args="[entityName]"/></title>
    </head>

    <body>
        <br/>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.create.label" args="[entityName]"/> - [${Util.ACTION_NEW}]</h4>
        </div>

        <div class="panel-body">

            <g:if test="${flash.errors}">
                <div class="alert alert-danger">${flash.errors}</div>
            </g:if>
            <g:hasErrors bean="${pedidoCargaInstance}">
                <div class="alert alert-danger">
                    <b>Erro ao salvar Pedido Carga</b>
                    <g:renderErrors bean="${pedidoCargaInstance}" as="list" />
                </div>
            </g:hasErrors>


            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>
            <g:link class="btn btn-default" action="list">
                <i class="glyphicon glyphicon-th-list"></i>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>

            <g:form method="POST" controller="pedidoCarga" name="defaultForm">
                <g:hiddenField name="id" value="${pedidoCargaInstance?.id}"/>
                <g:hiddenField name="valorCargaCategoria" value="${pedidoCargaInstance?.categoriasFuncionario?.valorCarga}"/>
                <g:hiddenField name="version" value="${pedidoCargaInstance?.version}"/>
                <g:hiddenField name="action" value="${action}"/>


                <g:render template="form" model="${[action: Util.ACTION_NEW]}"/>
                <button id="submitButton" type="submit" name="_action_save" class="btn btn-success">
                    <span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.create.label" default="Create"></g:message>
                </button>

            </g:form>
        </div>
    </div>


    <script type="application/javascript">
        var itensPedidos = [];
        var valorCategoria;

        $(document).ready(function () {
            valorCategoria = $("input#valorCargaCategoria").val();

            var defaultForm = $("form#defaultForm");
            //desabilita o formulario de ser submetido caso o botão 'enter' seja pressionado
            defaultForm.keypress(function(event){
                if (event.keyCode === 10 || event.keyCode === 13)
                    event.preventDefault();
            });

            //substitui os dados que serão enviado para o servidor
            defaultForm.submit(function (e) {
                waitingDialog.show();
                //remove os inputs desnecessários
                $("input[type=search],[type=checkbox]:not([name='selectAll'], [name*='func_'], [name*='veic_']), [type=text]:not([name='dataCarga'])", $(this)).remove();
                var esse = $(this);

                itensPedidos.forEach(function (item) {
                    if (item.ativo === true) esse.append("<input type='hidden' name='funcionariosAtivos' value='" + item.id + "'/>");
                    else esse.append("<input type='hidden' name='funcionariosInativos' value='" + item.id + "'/>");
                    esse.append("<input type='hidden' name='valorCarga[" + item.id + "]' value='" + item.valor + "'/>");
                });
            });


            //Carrega taxas de cartão a serem cobradas, caso existam
            //carregarTaxasCartao();

            const action = $("input[name=action]").val();
            if (action === 'novo') {
                carregarPerfisRecarga("${pedidoCargaInstance?.unidade?.id}");
                carregarFuncionarios("${pedidoCargaInstance?.unidade?.id}");
            }

            $("select[name=empresa]").change(function() {
                carregarTaxasRh($(this).val());
                carregarUnidades($(this).val());
            })

            $("select[name=unidade]").change(function() {
                carregarPerfisRecarga($(this).val());

                const vinculoCartao = $("select[name=empresa]").find(':selected').data('vinculocartao');

                if (vinculoCartao === 'Funcionário')
                    carregarFuncionarios($(this).val());
                else if (vinculoCartao === 'Máquina') {
                    carregarVeiculos($(this).val());
                }
            })


        });

        function carregarVeiculos(unidId) {
            $("div#pedidoFuncionarios").hide();
            $("div#pedidoVeiculos").show();


            var output = {
                id: $("input#id").val(),
                categoria: $('input[name=categoriaSelecionada]:checked').val(),
                actionView: $("input#action").val(),
                unidade: unidId
            };

            waitingDialog.show("Aguarde...");
            $.ajax({
                url: "${g.createLink(controller:'pedidoCarga', action:'listVeiculos')}",
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

            //seleciona a primeira categoria
            $('input:radio[name=categoriaSelecionada]:first').attr('checked', true);

        }

        function carregarPerfisRecarga(unidId) {

            $.getJSON("${createLink(controller: 'categoriaFuncionario', action: 'getAllByUnidade')}", { unidId: unidId },
                    function(data, status) {
                        $("#tabCateg > tbody").html("")

                        if (status === 'success') {
                            data.forEach(function(categ) {
                                $("#tabCateg > tbody:last-child").append("<tr><td><input type='radio' name='categoriaSelecionada' value='" + categ.id + "'"  +
                                        "onchange='filtrarFuncionarios()' class='enable'/></td>" +
                                        "<td>" + categ.nome + "</td>" +
                                        "<td>" + categ.valorCarga + "</td></tr>")
                            })
                        }
                    })

        }

        function carregarUnidades(rhId) {



            $.getJSON("${createLink(controller: 'unidade', action: 'getAllByRh')}", { rhId: rhId },
                    function(data, status) {
                        let selUnid = $("select[name=unidade]")
                        selUnid.html("")
                        if (status === 'success') {
                            let unidList = []
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
                url: "${g.createLink(controller:'pedidoCarga', action:'filterFuncionarios')}",
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
                url: "${g.createLink(controller:'pedidoCarga', action:'getAllFuncionariosIds')}",
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
            var valorItem = $("input#valorCarga-" + itemId).val();
            if (typeof valorItem == 'undefined') valorItem = valorCategoria.toString();
            valorItem = valorItem.replace(/,/g, '').replace(/\./g, '');
            valorItem = valorItem.slice(0, valorItem.length - 2) + '.' + valorItem.slice(valorItem.length - 2, valorItem.length);

            var checkbox = $("input[type='checkbox'][name='itensAtivos'][value=" + itemId + "]");
            if (checkbox.length == 0) checkbox = $("input[type='checkbox'][name='selectAll']");

            var itemPedido = {
                id: itemId,
                valor: valorItem,
                ativo: checkbox.is(":checked")
            };

            var index = -1;
            for (var i = 0; i < itensPedidos.length; i++) {
                if (itensPedidos[i].id === itemPedido.id) {
                    index = i;
                    break;
                }
            }
            //pedido não encontrado no array itensPedidos
            if (index < 0) {
                itensPedidos.push(itemPedido);
            }
            else {
                itensPedidos[index] = itemPedido
            }
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
                url: "${g.createLink(controller:'pedidoCarga', action:'listFuncionarios')}",
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

        function carregarTaxasCartao() {
            waitingDialog.show("Aguarde...");

            var params={
                unidId:$("#unidade_id").val(),
                pedId:$("#id").val()
            };

            $.ajax({
                url:"${createLink(controller: 'pedidoCarga', action:'loadTaxasCartao')}",
                data:params,
                dataType:'html',

                success:function(data){
                    $("div#taxasCartao").html(data);
                }
            });
        }

        function carregarTaxasRh(rhId) {
            $.getJSON("${createLink(controller: 'rh', action: 'getTaxas')}", { rhId: rhId },
                    function (data, status) {
                        if (status === 'success') {
                            $("p#taxaPedido").html(data.taxaPedido)
                        }
                    })
        }


    </script>





    </body>
</html>