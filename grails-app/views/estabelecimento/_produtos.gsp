<%@ page import="com.sysdata.gestaofrota.Util" %>

<br/>
<g:form method="post" >
    <g:hiddenField name="id" value="${estabelecimentoInstance?.id}" />
    <g:hiddenField name="version" value="${estabelecimentoInstance?.version}" />
    <g:hiddenField name="empId" value="${empresaInstance?.id}" />
    <g:hiddenField name="action" value="${action}"/>


    <div class="panel panel-default">
        <div class="panel-heading">
            Produtos
        </div>

        <div class="panel-body">

            <div class="list">

                    <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                    <g:if test="${prodEstabInstance}">
                        <thead>
                            <tr>

                                <g:sortableColumn property="codigo" title="${message(code: 'produto.codigo.label', default: 'Codigo')}" />

                                <g:sortableColumn property="nome" title="${message(code: 'produto.nome.label', default: 'Nome')}" />

                                <g:sortableColumn property="tipo" title="${message(code: 'produto.tipo.label', default: 'Tipo')}" />

                                <g:sortableColumn property="valor" title="${message(code: 'produto.valor.label', default: 'Valor')}" />

                            </tr>
                        </thead>
                        <tbody>



                                <g:each in="${prodEstabInstance}" status="i" var="produtoEstabelecimentoInstance">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                        <td>${fieldValue(bean: produtoEstabelecimentoInstance.produto, field: "codigo")}</td>

                                        <td>${fieldValue(bean: produtoEstabelecimentoInstance.produto, field: "nome")}</td>

                                        <td>${fieldValue(bean: produtoEstabelecimentoInstance.produto, field: "tipo")}</td>

                                        <td>${fieldValue(bean: produtoEstabelecimentoInstance, field: "valor")}</td>

                                    </tr>
                                </g:each>
                        </tbody>
                    </g:if>
                    <g:else>
                        SEM REGISTROS
                    </g:else>
                    </table>

    %{--<div class="pagination">
        <g:paginate total="${produtoInstanceTotal}" />
    </div>--}%
</g:form>


