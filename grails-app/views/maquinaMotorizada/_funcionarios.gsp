<script type="text/javascript" src="${resource(dir: 'js', file: 'plugins/bootbox/bootbox.min.js')}"></script>
<script type="text/javascript">
    var tabelaFuncionariosHabilitados;

    $(document).ready(function () {
        carregaFuncionariosHabilitados();
    });

    function carregaFuncionariosHabilitados() {
        tabelaFuncionariosHabilitados = $("table#funcionariosHabilitados").DataTable({
            "ajax": {
                "url": "${createLink(controller:'maquinaMotorizada', action:'listFuncionariosJSON')}",
                "data": {"id": ${instance?.id}},
                "dataSrc": "results"
            },
            "columns": [
                {"data": "cpf"},
                {"data": "nome"},
                {"data": "matricula"},
                {"data": "acao"}
            ]
        });
    }

    function removeFuncionario(idFunc) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'maquinaMotorizada', action:'removeFuncionario')}",
            data: {'id': ${instance?.id}, 'funcionario': idFunc, 'instanceName': '${instanceName}'},
            beforeSend: function () {
                waitingDialog.show("Removendo relação entre Funcionário e ${instanceName}...");
            },
            success: function (data) {
                $("#message").html("<div class='alert alert-success' role='alert'>" + data + "</div>");
                $("#message").show();
                $("#message").fadeOut(5000);
                //Recarrega a lista de funcionários relacionados a instância
                tabelaFuncionariosHabilitados.ajax.reload();
            },
            complete: function () {
                waitingDialog.hide();
            }
        });
    }

    function habilitarFuncionarios() {
        var output = {
            'id': ${instance?.id},
            'funcionarios': funcSel,
            'instanceName': '${instanceName}'
        };

        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'maquinaMotorizada', action:'addFuncionario')}",
            data: output,
            beforeSend: function () {
                waitingDialog.show("Incluindo relação entre Funcionário e ${instanceName}...");
            },
            success: function (data) {
                $("#message").html("<div class='alert alert-success' role='alert'>" + data + "</div>");
                $("#message").show();
                $("#message").fadeOut(5000);
                //Recarrega a lista de funcionários relacionados ao veículo
                tabelaFuncionariosHabilitados.ajax.reload();
            },
            complete: function () {
                waitingDialog.hide();
            }
        });
    }

    function openFuncModal(html) {
        bootbox.dialog({
            title: "Vinculação Funcionário ao ${instanceName}",

            message: html,

            buttons: {
                success: {
                    label: "Habilitar",
                    className: "btn-success",
                    callback: function () {
                        habilitarFuncionarios();
                    }
                }
            }
        });
    }

    function loadModalFuncionario() {
        var output = {
            'controller': "funcionario",
            'unidade_id': ${instance?.unidade?.id},
            'action': "filtro"
        };

        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'funcionario', action:'search')}",
            data: output,
            success: function (data) {
                openFuncModal(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });
    }
</script>

<div class="panel panel-default">
    <div class="panel-heading">Funcionários habilitados para condução</div>

    <div class="panel-body">
        <button class="btn btn-default" onclick="loadModalFuncionario()">
            <i class="glyphicon glyphicon-plus"></i>
            Habilitar Funcionário
        </button>

        <div id="message" style="margin-top: 10px"></div>

        <table id="funcionariosHabilitados" class="table table-striped table-bordered">
            <thead>
            <th>CPF</th>
            <th>Nome</th>
            <th>Matrícula</th>
            <th>Ação</th>
            </thead>
        </table>
    </div>
</div>