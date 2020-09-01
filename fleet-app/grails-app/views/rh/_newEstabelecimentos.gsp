<script type="application/javascript">
    $(function () {
        carregarComboBox();
        carregarTabela();
        verificarDisponibilidadeBotao();

        $('#estabs').change(function () {
            verificarDisponibilidadeBotao();
        });

        $('#addButton').click(function () {
            waitingDialog.show("Aguarde...");
            console.log($("select#estabs option:selected").val())
            $.ajax({
                url: "${g.createLink(controller:'rh', action:'salvarEstabelecimentosVinculados')}",
                data: { prgId:$("#rhId").val(), selEstId: $("select#estabs option:selected").val()},
                dataType: 'json',
                success: function (data) {
                    carregarComboBox();
                    carregarTabela();
                    document.getElementById("mensagem").innerHTML = "<div class='alert alert-info'>"+data.mensagem+"</div>";

                },
                complete: function () {
                    waitingDialog.hide();
                    verificarDisponibilidadeBotao();

                }

            })
        });

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
                            carregarTabela();
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

        function carregarTabela(){
            $.ajax({
                url: "${g.createLink(controller:'rh', action:'listEstabVinculados')}",
                data:{prgId:$("#rhId").val()},
                dataType: 'html',
                success: function (data) {
                    document.getElementById("estabTable").innerHTML = data;
                    detelarEstab();
                }
            })
        }

        function verificarDisponibilidadeBotao() {
            var selected = $("select#estabs option:selected").val();
            if(selected == 'none'){
                $("#addButton").prop('disabled', true);
            } else {
                $("#addButton").prop('disabled', false);
            }
        }

        function carregarComboBox() {
            $.ajax({
                url: "${g.createLink(controller:'rh', action:'listEstabNaoVinculados')}",
                data:{prgId:$("#rhId").val()},
                dataType: 'json',
                success: function (data){
                    updateSelectJSON("estabs", data, "Selecione um Estabelecimento");
                }
            })
        }


        function updateSelectJSON(selectId, jsonData, noSelectionLabel) {
            var selectComponent = $('select#' + selectId);
            selectComponent.find('option').remove();

            if (jsonData.length > 0) {
                selectComponent.append("<option value='none'>" + noSelectionLabel + '</option>');

                $.each(jsonData, function (key, value) {
                    selectComponent.append('<option value=' + value.id + '>' + value.nome + '</option>');
                });
            }
            else {
                selectComponent.append("<option value='none'>NADA ENCONTRADO</option>");

            }
        }
    });
</script>
<g:hiddenField name="rhId" value="${rhInstance?.id}"/>
<div id="mensagem"></div>

<div class="row">
    <div class="col-md-6">
        <div class="input-group">
            <select name="estabelecimentos" id="estabs" class="form-control enable">
                <option value="none">Selecionar Estabelecimento</option>
            </select>
            <span class="input-group-btn">
                <button class="btn btn-default" type="button" id="addButton">Adicionar</button>
            </span>
        </div>



    </div>
</div>
<hr/>
<div id="estabTable"></div>