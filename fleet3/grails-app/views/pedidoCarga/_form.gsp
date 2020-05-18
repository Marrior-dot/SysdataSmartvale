<%@ page import="java.text.SimpleDateFormat; com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>


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
            $("input[type=search],[type=checkbox]:not([name='selectAll']),[type=text]:not([name='dataCarga'])", $(this)).remove();
            var esse = $(this);

            itensPedidos.forEach(function (item) {
                if (item.ativo === true) esse.append("<input type='hidden' name='funcionariosAtivos' value='" + item.id + "'/>");
                else esse.append("<input type='hidden' name='funcionariosInativos' value='" + item.id + "'/>");
                esse.append("<input type='hidden' name='valorCarga[" + item.id + "]' value='" + item.valor + "'/>");
            });
        });

        //seleciona a primeira categoria
        $('input:radio[name=categoriaSelecionada]:first').attr('checked', true);
        selecionaCategoria();

        //Carrega taxas de cartão a serem cobradas, caso existam
        carregarTaxasCartao();
    });

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

    function setItemPedido(funcionarioId) {
        //formata valor da carga
        var valorItem = $("input#valorCarga-" + funcionarioId).val();
        if (typeof valorItem == 'undefined') valorItem = valorCategoria.toString();
        valorItem = valorItem.replace(/,/g, '').replace(/\./g, '');
        valorItem = valorItem.slice(0, valorItem.length - 2) + '.' + valorItem.slice(valorItem.length - 2, valorItem.length);

        var checkbox = $("input[type='checkbox'][name='funcionariosAtivos'][value=" + funcionarioId + "]");
        if (checkbox.length == 0) checkbox = $("input[type='checkbox'][name='selectAll']");

        var itemPedido = {
            id: funcionarioId,
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

    function selecionaCategoria() {
        var output = {
            id: $("input#id").val(),
            categoria: $('input[name=categoriaSelecionada]:checked').val(),
            actionView: $("input#action").val()
        };

        waitingDialog.show("Aguarde...");
        $.ajax({
            url: "${g.createLink(controller:'pedidoCarga', action:'filterFuncionarios')}",
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


</script>

<g:hiddenField name="id" value="${pedidoCargaInstance?.id}"/>
<g:hiddenField name="unidade_id" value="${pedidoCargaInstance?.unidade?.id}"/>
<g:hiddenField name="valorCargaCategoria" value="${pedidoCargaInstance?.categoriasFuncionario?.valorCarga}"/>
<g:hiddenField name="version" value="${pedidoCargaInstance?.version}"/>
<g:hiddenField name="action" value="${action}"/>







<table class="table table-bordered">
    <tbody>
    <tr>
        <td>RH</td>
        <td><b>${pedidoCargaInstance?.unidade?.rh?.nome}</b></td>
    </tr>
    <tr>
        <td>Unidade</td>
        <td><b>${pedidoCargaInstance?.unidade?.nome}</b></td>
    </tr>
    </tbody>
</table>




<div class="form-horizontal row">
    <div class="col-xs-3 input-group-sm">
        <label class="control-label" for="dataCarga">Data de Carga</label>
        <input type="text" id="dataCarga" name="dataCarga" class="form-control datepicker"
               value="${Util.formattedDate(pedidoCargaInstance?.dataCarga)}"/>
    </div>

    <div class="col-xs-3 input-group-sm">
        <label class="control-label">Taxa Pedido</label>
        <p class="form-control-static">${pedidoCargaInstance?.taxa ?: 0}%</p>
    </div>
    <g:if test="${action != Util.ACTION_NEW && pedidoCargaInstance}">
        <div class="col-xs-3 input-group-sm">
            <label class="control-label">Total Pedido (+ taxa)</label>
            <p class="form-control-static"><g:formatReal value="${pedidoCargaInstance?.total}"/></p>
        </div>
    </g:if>
</div>

<g:render template="/categoriaFuncionario/list"
          model="${[categoriaFuncionarioInstanceList: pedidoCargaInstance?.categoriasFuncionario]}"/>


<g:render template="funcionarios" model="${[pedidoCargaInstance: pedidoCargaInstance, action: action]}"/>


<g:render template="taxasCartao" />

<br/>