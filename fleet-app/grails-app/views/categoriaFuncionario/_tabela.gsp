<%@ page import="com.sysdata.gestaofrota.TipoCobranca" %>
<table class="table table-bordered table-striped" id="categorias">
    <thead>
    <tr>
        <th>Nome</th>
        <g:if test="${rhInstance.modeloCobranca == TipoCobranca.PRE_PAGO}">
            <th>Valor Carga</th>
        </g:if>
        <th class="text-center">
            Opções
            <button style="float: right" type="button" class="btn btn-default btn-sm" title="Adicionar Nova Categoria"
                    data-toggle="modal" data-target="#nova-categoria-modal">
                <i class="glyphicon glyphicon-plus"></i>
            </button>
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${rhInstance?.categoriasFuncionario?.sort { it.dateCreated }}" var="categoria" status="i">
        <tr>
            <td>${categoria.nome}</td>
            <g:if test="${rhInstance.modeloCobranca == TipoCobranca.PRE_PAGO}">
                <td><g:formatNumber number="${categoria.valorCarga}" type="currency" locale="pt_BR"/></td>
            </g:if>
            <td class="text-center">
                <button type="button" class="btn btn-default" title="Editar" onclick="editarCategoria(${i}, ${categoria.id})">
                    <i class="glyphicon glyphicon-edit"></i>
                </button>

                <button type="button" class="btn btn-danger" title="Excluir" onclick="deleteCategoria(${categoria.id})">
                    <i class="glyphicon glyphicon-remove"></i>
                </button>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>