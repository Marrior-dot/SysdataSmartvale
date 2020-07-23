<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:if test="${veiculoInstanceCount > 0}">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Seleção</th>
            <th>Placa</th>
            <th>Marca</th>
            <th>Modelo</th>
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
                <td>${veiculo?.placa}</td>
                <td>${veiculo?.marca}</td>
                <td>${veiculo?.modelo}</td>

                <g:if test="${action == Util.ACTION_VIEW}">
                    <td>R$ ${pedidoCargaInstance?.valorInPedido(veiculo)}</td>
                </g:if>
                <g:else>
                    <td>
                        <input type="text" class="form-control decimal" id="valorCarga_${veiculo.id}"
                               value="${pedidoCargaInstance?.valorInPedido(veiculo)}"
                               onchange="setItemPedido(${veiculo?.id})"/>
                    </td>
                </g:else>
            </tr>
        </g:each>
        </tbody>
    </table>

    <util:remotePaginate controller="pedidoCarga" action="listVeiculos"
                         total="${veiculoInstanceCount}"
                         params="${[actionView: action, categoria: categoriaInstance?.id]}"
                         update="veiculo-list" max="10" id="${pedidoCargaInstance?.id}"
                         onLoading="waitingDialog.show('Aguarde...')"
                         onComplete="onFuncionarioListLoadComplete()"/>
</g:if>
<g:else>
    <div class="well well-lg text-center"><b>SEM DADOS</b></div>
</g:else>