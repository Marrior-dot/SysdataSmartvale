<script type="application/javascript">
    $(document).ready(function () {
        atualizarTabelaFechamentos();
        var fechamentoForm = $("form#fechamento");
        submitFormByAjax(fechamentoForm, 1, function (data, form) {
            atualizarTabelaFechamentos();
        });
    });

    function atualizarTabelaFechamentos() {
        var fechamentoIndexUrl = $("input:hidden#fechamento-index-url").val();
        var programaId = $("input:hidden#programa\\.id").val();
        $.get(fechamentoIndexUrl, {'programa.id': programaId}, function (data) {
            $("div#fechamento-index").html(data);
        });
    }

    function removerFechamento(id) {
        swal({
            title: "Você tem certeza?",
            type: "warning",
            showCancelButton: true,
            confirmButtonClass: "btn-danger",
            confirmButtonText: "Sim",
            cancelButtonText: "Não",
            closeOnConfirm: true
        },
        function(){
            var fechamentoDeleteUrl = $("input:hidden#fechamento-delete-url").val();
            $.ajax ({
                url: fechamentoDeleteUrl + '/' + id,
                method: "DELETE",
            }).done(function () {
                atualizarTabelaFechamentos();
            });
        });
    }

    function abrirCortes(id) {
        $.ajax({
            url:"${createLink(controller:'fechamento',action:'abrirCortes')}/"+id,
            method:'POST',
            success:function(data){
                $("#fechamento-index").html(data);
            }
        })
    }


</script>

<div class="row">
    <g:hiddenField name="fechamento-index-url" value="${g.createLink(controller: 'fechamento', action: 'index')}"/>
    <g:hiddenField name="fechamento-delete-url" value="${g.createLink(controller: 'fechamento', action: 'delete')}"/>

    <g:form name="fechamento" controller="fechamento" action="save">
        <g:hiddenField name="programa.id" value="${rhInstance.id}"/>
        <div class="form-group col-md-6">
            <label for="diaCorte">Adicionar Novo Fechamento</label>
            <div class="input-group">
                <g:select name="diaCorte" from="${1..31}" class="form-control enable" noSelection="['': 'Dia do Corte']"
                          required="required"/>
                <span class="input-group-addon">|</span>
                <g:select name="diasAteVencimento" from="${1..30}" class="form-control enable"
                          noSelection="['': 'Dias até o Vencimento']" required="required"/>
                <span class="input-group-btn">
                    <button class="btn btn-default" type="submit">Adicionar</button>
                </span>
            </div>
        </div>
    </g:form>
</div>
<hr>

<div class="row" id="fechamento-index">
    <g:render template="/fechamento/index"/>
</div>