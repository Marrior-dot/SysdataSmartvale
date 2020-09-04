<%@ page import="com.sysdata.gestaofrota.Estado" %>
<script type="application/javascript">
    $(function () {

        carregarEstadosEstabelecimentos();
        carregarEstabsVinculados();


        $("select[name=estados]").change(function() {
            carregarCidadesEstabelecimentos($(this).val());
        });


        $("input[name=fantasia]").change(function() {
            carregarEstabsVinculados($(this).val());
        });


        $("button[name=editEstabs]").click(function() {

            var nome = $("input[name=fantasia]").val();
            var ufsIds = $("select[name=estados]").val();

            carregarEstabsEdicao(nome, ufsIds);

            $(this).hide();
            $("button[name=saveEstabs]").show();
        });

        function carregarEstadosEstabelecimentos() {

            $.getJSON("${createLink(controller: 'estado', action: 'listAllByEstabelecimento')}",

                function(estabs) {
                    var estabSelect = $("select[name=estados]");
                    var options = "";
                    estabs.forEach(function(est) {
                        options += "<option value='" +est.id+ "'>" + est.nome + "</option>"
                    });
                    estabSelect.html(options);
                    estabSelect.selectpicker();
                }
            );
        }


        function carregarCidadesEstabelecimentos(arrIds) {

            $.get("${createLink(controller: 'cidade', action: 'listAllByEstabelecimento')}",
                    {ufs: arrIds}
            )
            .done(function(gsp) {
                $("div#divCidades").html(gsp);
                $("select[name=cidades]").selectpicker();
            })
            .always(function() {
                // Carrega ECs vinculados por UFs && Nome Fantasia
                var fantasia = $("input[name=fantasia]").val();
                carregarEstabsVinculados(fantasia, arrIds);
            });
        }


        function detelarEstab(){
            $("[name='remover']").on('click', function () {
                var confirmacao = confirm('Você tem certeza?');
                if(confirmacao){
                    waitingDialog.show("Aguarde...");
                    var estabId = $(this).attr('id').replace('remover', '');
                    $.ajax({
                        url: "${g.createLink(controller:'rh', action:'desvincularEstab')}",
                        data:{prgId:$("#rhId").val(), selectedEstabId: estabId},
                        dataType: 'json',
                        success: function (data) {
                            carregarComboBox();
                            carregarEstabsVinculados();
                            document.getElementById("mensagem").innerHTML = "<div class='alert alert-info'>"+data.mensagem+"</div>";
                        },
                        complete: function () {
                            waitingDialog.hide();
                            verificarDisponibilidadeBotao();
                        }
                    })
                } else {

                }

            });
        }

        function carregarEstabsVinculados(nome, ufsIds){
            var rhId = $("#rhId").val();
            $.ajax({
                url: "${g.createLink(controller:'rh', action:'listEstabsVinculados')}",
                data: { rhId: rhId, fantasia: nome, ufs: ufsIds},
                dataType: 'html',
                success: function (data) {
                    document.getElementById("estabTable").innerHTML = data;
                }
            });
        }

        function carregarEstabsEdicao(nome, ufsIds){
            var rhId = $("#rhId").val();
            $.ajax({
                url: "${g.createLink(controller:'rh', action:'editEstabsVinculados')}",
                data: { rhId: rhId, fantasia: nome, ufs: ufsIds},
                dataType: 'html',
                success: function (data) {
                    document.getElementById("estabTable").innerHTML = data;
                }
            });
        }

    });
</script>

<g:hiddenField name="rhId" value="${rhInstance?.id}"/>
<div id="mensagem"></div>

<div class="panel">
    <div class="panel panel-default">

        <div class="panel-heading">
            Filtros
        </div>

        <div class="panel-body">
            <div class="row">
                <div class="col-md-6">
                    <label for="estados">Estado</label>
                    <select name="estados" class="form-control selectpicker enable" multiple>
                    </select>
                </div>

                <div class="col-md-6">
                    <div id="divCidades">
                        <g:render template="/cidade/cidadesSelect"></g:render>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <label for="fantasia">Nome Fantasia</label>
                    <g:textField name="fantasia" class="form-control enable" ></g:textField>
                </div>
            </div>

        </div>

    </div>

    <div id="estabTable"></div>

    <div class="panel-footer">
        <button type="button" name="editEstabs" class="btn btn-default"><i class="glyphicon glyphicon-edit"></i>&nbsp;Editar</button>
        <button type="button" name="saveEstabs" class="btn btn-success" style="display: none;"><i class="glyphicon glyphicon-save"></i>&nbsp;Salvar</button>
    </div>
</div>



