<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:if test="${funcionarioInstanceCount > 0}">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Seleção</th>
            <th>Matrícula</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Valor Carga</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${funcionarioInstanceList}" var="funcionario">
            <tr>
                <td>
                    <input type="checkbox" class="checkbox" name="func_${funcionario.id}"
                           onchange="setItemPedido(${funcionario.id})" ${funcionario?.isAtivoInPedido(pedidoCargaInstance) || action == Util.ACTION_NEW ? 'checked' : ''} />
                </td>
                <td>${funcionario?.matricula}</td>
                <td>${funcionario?.nome}</td>
                <td>${funcionario?.cpf}</td>

                <g:if test="${action == Util.ACTION_VIEW}">
                    <td>R$ ${funcionario?.valorInPedido(pedidoCargaInstance)}</td>
                </g:if>
                <g:else>
                    <td>
                        <input type="text" class="form-control money" id="valorCarga_${funcionario.id}"
                               value="${funcionario?.valorInPedido(pedidoCargaInstance)}"
                               onchange="setItemPedido(${funcionario?.id})"/>
                    </td>
                </g:else>
            </tr>
        </g:each>
        </tbody>
    </table>

    <util:remotePaginate controller="pedidoCarga" action="filterFuncionarios"
                         total="${funcionarioInstanceCount}"
                         params="${[actionView: action, categoria: categoriaInstance?.id]}"
                         update="funcionario-list" max="10" id="${pedidoCargaInstance?.id}"
                         onLoading="waitingDialog.show('Aguarde...')"
                         onComplete="onFuncionarioListLoadComplete()"/>
</g:if>
<g:else>
    <div class="well well-lg text-center"><b>SEM DADOS</b></div>
</g:else>