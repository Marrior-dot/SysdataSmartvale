<%@ page import="com.sysdata.gestaofrota.TipoCobranca" %>
<script type="application/javascript">
    $(document).ready(function () {
        var novaCategoriaForm = $("form#nova-categoria");
        var editCategoriaForm = $("form#edit-categoria");
        var deleteCategoriaForm = $("form#delete-categoria");

        submitCategoriaForm(novaCategoriaForm);
        submitCategoriaForm(editCategoriaForm);
        submitCategoriaForm(deleteCategoriaForm);
    });

    function submitCategoriaForm(form) {
        submitFormByAjax(form, 0, function (data) {
            $("div#nova-categoria-modal").modal('hide');
            $("div#edit-categoria-modal").modal('hide');
            $("div#tabela-container").html(data);
        }, null, true, "html");
    }

    function editarCategoria(index, id) {
        var modalComponent = $("div#edit-categoria-modal");
        var editCategoriaForm = modalComponent.find("form#edit-categoria");
        var tableData = $('table#categorias tbody tr:eq(' + index + ') td');
        var nome = tableData[0].innerText;
        editCategoriaForm.find("input#nome").val(nome);
        editCategoriaForm.find("input:hidden#id").val(id);

        // input valor carga pode não existir caso tipo de cobraça != Pre Pago
        var valorCargaInput = editCategoriaForm.find("input#valorCarga");
        if (valorCargaInput) {
            var valor = tableData[1].innerText;
            editCategoriaForm.find("input#valorCarga").val(valor);
        }

        modalComponent.modal('show');
    }

    function deleteCategoria(id) {
        var deleteCategoriaForm = $("form#delete-categoria");
        deleteCategoriaForm.find("input:hidden#id").val(id);
        deleteCategoriaForm.submit();
    }
</script>

<g:if test="${rhInstance.modeloCobranca == TipoCobranca.POS_PAGO}">
    <div class="alert alert-warning" role="alert">
        <strong>Atenção:</strong> perfis não possuem valores devido seu modelo de cobrança ser <strong>${rhInstance.modeloCobranca.nome}</strong>.
    </div>
</g:if>


<div id="tabela-container">
    <g:render template="/categoriaFuncionario/tabela" model="[rhInstance: rhInstance]"/>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="nova-categoria-modal">
    <div class="modal-dialog" role="document" style="z-index: 6000">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Novo Perfil</h4>
            </div>

            <div class="modal-body">
                <g:form name="nova-categoria" class="row" controller="categoriaFuncionario" action="save">
                    <input type="hidden" name="rh" id="rh" value="${rhInstance.id}">

                    <div class="form-group col-md-6">
                        <label for="nome">Nome *</label>
                        <input type="text" class="form-control enable" name="nome" id="nome" required>
                    </div>

                    <g:if test="${rhInstance.modeloCobranca == TipoCobranca.PRE_PAGO}">
                        <div class="form-group col-md-6">
                            <label for="valorCarga">Valor *</label>
                            <input type="text" class="form-control money enable" name="valorCarga" id="valorCarga"
                                   required>
                        </div>
                    </g:if>
                </g:form>
            </div>

            <div class="modal-footer text-right">
                <button class="btn btn-default" type="submit" form="nova-categoria">
                    <i class="glyphicon glyphicon-ok"></i> Enviar
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="edit-categoria-modal">
    <div class="modal-dialog" role="document" style="z-index: 6000">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Editar Perfil</h4>
            </div>

            <div class="modal-body">
                <g:form name="edit-categoria" class="row" controller="categoriaFuncionario" action="update">
                    <input type="hidden" name="id" id="id">

                    <div class="form-group col-md-6">
                        <label for="nome">Nome *</label>
                        <input type="text" class="form-control enable" name="nome" id="nome" required>
                    </div>

                    <g:if test="${rhInstance.modeloCobranca == TipoCobranca.PRE_PAGO}">
                        <div class="form-group col-md-6">
                            <label for="valorCarga">Valor *</label>
                            <input type="text" class="form-control money enable" name="valorCarga" id="valorCarga"
                                   required>
                        </div>
                    </g:if>
                </g:form>
            </div>

            <div class="modal-footer text-right">
                <button class="btn btn-default" type="submit" form="edit-categoria">
                    <i class="glyphicon glyphicon-ok"></i> Enviar
                </button>
            </div>
        </div>
    </div>
</div>

<g:form name="delete-categoria" controller="categoriaFuncionario" action="delete" method="DELETE">
    <input type="hidden" name="id" id="id">
</g:form>
