<%@ page import="com.sysdata.gestaofrota.Util" %>
<g:form controller="produtoEstabelecimento" action="save" method="POST">
    <g:hiddenField name="estabelecimento.id" value="${estabelecimentoInstance?.id}"/>

    <div class="panel panel-default panel-top">
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
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${produtoList}" var="produto" status="i">
                        <g:set var="prodEst" value="${produtoEstabelecimentoList.find {it.produto.id == produto.id}}"/>
                        <g:hiddenField name="produto[${i}].id" value="${prodEst?.id}"></g:hiddenField>
                        <g:hiddenField name="produto[${i}].produto.id" value="${produto?.id}"></g:hiddenField>
                        <tr>
                            <td class="text-center">
                                <g:checkBox name="produto[${i}].ativo" value="${prodEst?.ativo}" />
                            </td>
                            <td>${fieldValue(bean: produto, field: "codigo")}</td>
                            <td>${fieldValue(bean: produto, field: "nome")}</td>
                            <td>${produto.tipo?.nome}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="well text-center"><strong>Sem Produtos vinculados</strong></div>
        </g:else>
    </div>

    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
        <g:if test="${action in [Util.ACTION_EDIT, Util.ACTION_NEW]}">
            <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-save"></span>&nbsp;Salvar Produtos</button>
        </g:if>
        <g:elseif test="${action == Util.ACTION_VIEW}">
            <button id="btnEditProd" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span>&nbsp;Editar Produtos</button>
        </g:elseif>
    </sec:ifAnyGranted>
</g:form>


