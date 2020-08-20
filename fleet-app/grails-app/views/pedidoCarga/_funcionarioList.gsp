<%@ page import="com.sysdata.gestaofrota.Util" %>

<style>
    .alert.message {
        display: none;
        height: 1em;
        text-align: center;
        padding-top: 5px;
        border: 1px solid;
    }
</style>


<g:if test="${funcionarioInstanceCount > 0}">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Seleção</th>
            <th>Matrícula</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Valor Carga</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${funcionarioInstanceList}" var="funcionario">
            <tr>
                <td>
                    <input type="checkbox" class="checkbox" name="func_${funcionario.id}"
                           onchange="setItemPedido(${funcionario.id})" ${pedidoCargaInstance?.isFuncionarioInPedido(funcionario) || action == Util.ACTION_NEW ? 'checked' : ''} />
                </td>
                <td>${funcionario?.matricula}</td>
                <td>${funcionario?.nome}</td>
                <td>${funcionario?.cpf}</td>

                <g:if test="${action == Util.ACTION_VIEW}">
                    <td>${pedidoCargaInstance?.valorInPedido(funcionario)}</td>
                </g:if>
                <g:else>
                    <td>
                        <g:textField name="valorCarga_${funcionario.id}" class="form-control money"
                                    value="${pedidoCargaInstance?.valorInPedido(funcionario)}"
                                    data-oldvalue="${pedidoCargaInstance?.valorInPedido(funcionario)}"
                                    onblur="setItemPedido(${funcionario?.id})" />
                    </td>

                    <td><div id="msg_${funcionario.id}" class="alert alert-danger message" role="alert" ></div></td>
                </g:else>
            </tr>
        </g:each>
        </tbody>
    </table>

%{--
    <util:remotePaginate controller="pedidoCarga" action="listFuncionarios"
                         total="${funcionarioInstanceCount}"
                         params="${[actionView: action, categoria: categoriaInstance?.id]}"
                         update="funcionario-list" max="10" id="${pedidoCargaInstance?.id}"
                         onLoading="waitingDialog.show('Aguarde...')"
                         onComplete="onFuncionarioListLoadComplete()"/>
--}%
</g:if>
<g:else>
    <div class="well well-lg text-center"><b>SEM DADOS</b></div>
</g:else>