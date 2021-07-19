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
                <td>R$ ${Util.toBigDecimal(tx?.valor)}</td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <util:remotePaginate controller="pedidoCarga" action="loadTaxasCartao"
                         total="${taxasCount}"
                         params="${[unidId:unidadeInstance?.id,pedId:pedidoCargaInstance?.id]}"
                         update="taxasCartao" max="10"
                         onLoading="waitingDialog.show('Aguarde...')"
                         />



</g:if>
<g:else>
    <div class="well well-lg text-center"><b>SEM DADOS</b></div>
</g:else>