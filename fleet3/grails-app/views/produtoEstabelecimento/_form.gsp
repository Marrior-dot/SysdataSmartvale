<%@ page import="com.sysdata.gestaofrota.Util" %>
<g:form controller="produtoEstabelecimento" action="save" method="POST">
    <g:hiddenField name="estabelecimento.id" value="${estabelecimentoInstance?.id}"/>

    <div class="panel panel-default">
        <div class="panel-heading">
            Produtos Disponíveis
        </div>
        <g:if test="${produtoList?.size() > 0}">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Ativo</th>
                        <th>Código</th>
                        <th>Nome</th>
                        <th>Tipo</th>
                        <th>Valor (R$)</th>
                        <th>Valor Anterior(R$)</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${produtoList}" var="produto">
                        <g:set var="prodEst" value="${produtoEstabelecimentoList.find {it.produto.id == produto.id}}"/>
                        <tr>
                            <td class="text-center">
                                <input type="checkbox" name="produtosSelecionados" value="${produto.id}" ${prodEst?.ativo ? 'checked' : ''} />
                            </td>
                            <td>${fieldValue(bean: produto, field: "codigo")}</td>
                            <td>${fieldValue(bean: produto, field: "nome")}</td>
                            <td>${produto.tipo?.nome}</td>
                            <td><input type="text" class="form-control money" name="valor[${produto.id}]" value="${Util.formatCurrency(prodEst?.valor)}"/></td>
                            <td><input type="text" class="form-control money" name="valorAnterior" value="${prodEst?.valorAnterior ?: 0}" disabled /></td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="text-center"><strong>SEM REGISTROS</strong></div>
        </g:else>
    </div>


    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
        <g:if test="${action in [Util.ACTION_EDIT, Util.ACTION_NEW]}">
            <button type="submit" class="btn btn-default">Salvar Produtos</button>
        </g:if>
        <g:elseif test="${action == Util.ACTION_VIEW}">

            <button id="btnEditProd" type="button" class="btn btn-default">Editar</button>

        </g:elseif>
    </sec:ifAnyGranted>
</g:form>


