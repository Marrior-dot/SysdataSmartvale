<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:if test="${taxasCount>0}">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Descrição</th>
            <th>Valor</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${taxasList}" var="tx">
            <tr>
                <td>${tx?.descricao}</td>
                <td>R$ ${Util.toBigDecial(tx?.valor)}</td>
            </tr>
        </g:each>
        </tbody>
    </table>


%{--
    <util:remotePaginate controller="pedidoCarga" action="filterFuncionarios"
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