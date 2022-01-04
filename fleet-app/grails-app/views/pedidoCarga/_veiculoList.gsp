<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:if test="${veiculoInstanceCount > 0}">

    <g:checkBox name="selectAll" value="${true}" /> <strong>Marcar Todos</strong>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Seleção</th>
            <th>Máquina</th>
            <th>Saldo Atual</th>
            <th>Valor Carga</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${veiculoInstanceList}" var="veiculo">
            <tr>
                <td>
                    <input type="checkbox" class="checkbox" name="veic_${veiculo.id}"
                           onchange="setItemPedido(${veiculo.id})" ${pedidoCargaInstance?.isVeiculoInPedido(veiculo) || action == Util.ACTION_NEW ? 'checked' : ''} />
                </td>
                <td>${veiculo?.nomeEmbossing}</td>
                <td><g:formatNumber number="${veiculo?.portador?.saldoTotal}" type="currency"/></td>
                <g:if test="${action == Util.ACTION_VIEW}">
                    <td>R$ ${pedidoCargaInstance?.valorInPedido(veiculo)}</td>
                </g:if>
                <g:else>
                    <td>
                        <g:textField name="valorCarga_${veiculo.id}" class="form-control money"
                                     value="${pedidoCargaInstance?.valorInPedido(veiculo)}"
                                     data-oldvalue="${pedidoCargaInstance?.valorInPedido(veiculo)}"
                                     onblur="setItemPedido(${veiculo?.id})" />
                    </td>

                    <td><div id="msg_${veiculo.id}" class="alert alert-danger message" role="alert" ></div></td>

                </g:else>
            </tr>
        </g:each>
        </tbody>
    </table>

%{--
    <util:remotePaginate controller="pedidoCarga" action="listVeiculos"
                         total="${veiculoInstanceCount}"
                         params="${[actionView: action, categoria: categoriaInstance?.id]}"
                         update="veiculo-list" max="10" id="${pedidoCargaInstance?.id}"
                         onLoading="waitingDialog.show('Aguarde...')"
                         onComplete="onFuncionarioListLoadComplete()"/>
--}%
</g:if>
<g:else>
    <div class="well well-lg text-center"><b>SEM DADOS</b></div>
</g:else>

<script>
    $("#selectAll").click(function(e) {
        var checked = $(this).is(":checked");
        var table = $("table");
        table.find("tbody > tr").each(function() {
           $(this).find("td > :checkbox").each(function() {

               $(this).prop('checked', checked);
           });
        });
    });
</script>