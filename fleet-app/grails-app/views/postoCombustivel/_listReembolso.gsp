<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoIntervalo" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoSemanal" %>
<%@ page import="com.sysdata.gestaofrota.TipoReembolso" %>

<style>
.modal-dialog {
    z-index: 1500;
}

</style>


<br/>

<div class="row">
    <div class="col-md-3">
        <label for="tipoReembolso">Tipo Reembolso</label>

        <g:select name="tipoReembolso"
                  class="form-control enable"
                  from="${TipoReembolso.values()}"
                  optionValue="nome"
                  value="${postoCombustivelInstance?.tipoReembolso}"></g:select>


    </div>

    <g:if test="${!postoCombustivelInstance?.reembolsos || postoCombustivelInstance?.tipoReembolso == TipoReembolso.INTERVALOS_MULTIPLOS}">
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
            <div class="col-md-3">
                <button type="button" class="btn btn-default" onclick="openModal(0);">
                    <i class="glyphicon glyphicon-plus"></i>&nbsp;Adicionar Reembolso
                </button>
            </div>
        </sec:ifAnyGranted>
    </g:if>



</div>




<div id="divSemanal" class="panel-top">

    <div class="list">
        <table id="rbSemanalTable" class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
            <th>Dia Semana</th>
            <th>Intervalo Dias</th>
            <th>Açoes</th>
            </thead>
        </table>
    </div>

</div>

<div id="divIntervalo">

    <div class="list">
        <table id="rbIntervaloTable"
               class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
            <th>Inicio Intervalo</th>
            <th>Fim Intervalo</th>
            <th>Dia Efetivaçao</th>
            <th>Meses</th>
            <th>Açoes</th>
            </thead>
        </table>
    </div>

</div>

<div id="divDias">
</div>


<asset:javascript src="plugins/bootbox/bootbox.min.js"></asset:javascript>

<script type="text/javascript">

    var rbSemanalTable, rbIntervaloTable

    var checked = null;

    var errorList = [];

    $(function () {

        rbSemanalTable = $("#rbSemanalTable").DataTable({
            "ajax": {
                "url": "${createLink(controller:'postoCombustivel',action:'getReembolsoSemanal')}",
                "data": {"id":${postoCombustivelInstance?.id}},
                "dataSrc": "results"
            },
            "columns": [
                {"data": "diaSemana"},
                {"data": "intervaloDias"},
                {"data": "acao"}
            ]
        });

        rbIntervaloTable = $("#rbIntervaloTable").DataTable({
            "ajax": {
                "url": "${createLink(controller:'postoCombustivel',action:'getIntervalosReembolso')}",
                "data": {"id":${postoCombustivelInstance?.id}},
                "dataSrc": "results"
            },
            "columns": [
                {"data": "inicio"},
                {"data": "fim"},
                {"data": "diaEfetivacao"},
                {"data": "meses"},
                {"data": "acao"}
            ]
        });

        checked = $("input[name='tipoReembolso']:checked");

        <g:if test="${postoCombustivelInstance?.tipoReembolso == TipoReembolso.INTERVALOS_MULTIPLOS}">
            $("#divIntervalo").show();
            $("#divSemanal").hide();
            $("#divDias").hide();
        </g:if>

        <g:elseif test="${postoCombustivelInstance?.tipoReembolso == TipoReembolso.SEMANAL}">
            $("#divIntervalo").hide();
            $("#divSemanal").show();
            $("#divDias").hide();
        </g:elseif>

        <g:elseif test="${postoCombustivelInstance?.tipoReembolso == TipoReembolso.DIAS_TRANSCORRIDOS}">
            $("#divDias").show();
            $("#divIntervalo").hide();
            $("#divSemanal").hide();

            loadReembDias("${postoCombustivelInstance?.id}");
        </g:elseif>

        <g:else>
            $("#divIntervalo").hide();
            $("#divSemanal").hide();
            $("#divDias").hide();
            checked = null;
        </g:else>


        $("input[name='tipoReembolso']").change(function () {
            checked = $("input[name='tipoReembolso']:checked");
            if (checked.val() == 'INTERVALOS_MULTIPLOS') {
                $("#divIntervalo").show();
                $("#divSemanal").hide();
                $("#divDias").hide();
            } else if (checked.val() == 'SEMANAL') {
                $("#divIntervalo").hide();
                $("#divDias").hide();
                $("#divSemanal").show();
            } else if (checked.val() == 'DIAS_TRANSCORRIDOS') {
                $("#divDias").show();
                $("#divIntervalo").hide();
                $("#divSemanal").hide();
            }
        })
    });

    function deleteReembolso(rbId) {
        var del = confirm('Confirma a exclusão do Reembolso?');
        if (del) {
            $.ajax({
                type: 'POST',
                url: "${createLink(controller:'postoCombustivel',action:'deleteReembolso') }",
                data: "id=" + rbId,
                success: function (o) {
                    if (o.type == "ok") {
                        alert(o.message);

                        if (checked.val() == 'SEMANAL')
                            rbSemanalTable.ajax.reload();
                        else if (checked.val() == 'INTERVALOS_MULTIPLOS')
                            rbIntervaloTable.ajax.reload();
                        else if (checked.val() == 'DIAS_TRANSCORRIDOS')
                            rbDiasTable.ajax.reload();

                    }
                    else if (o.type == "error")
                        alert(o.message);
                },
                statusCode: {
                    404: function () {
                        alert("Falha ao abrir página para inclusão de Novos Funcionários");
                    }
                }
            });
        }
    }

    function openReembSemanal(html) {
        bootbox.dialog({
            title: "Reembolso Semanal",
            message: html,
            buttons: {
                success: {
                    label: "Salvar",
                    className: "btn-success",
                    callback: function () {
                        saveReembSemanal();
                    }
                }
            }
        });
    }

    function openReembIntervalo(html) {
        bootbox.dialog({
            title: "Intervalos Multiplos",
            message: html,
            buttons: {
                success: {
                    label: "Salvar",
                    className: "btn-success",
                    callback: function () {
                        saveReembIntervalos();
                    }
                }
            }
        });
    }

    function openReembDiasDialog(html) {
        bootbox.dialog({
            title: "Dias Transcorridos",
            message: html,
            buttons: {
                cancel: {
                    label: "Cancelar",
                    className: "btn-cancel"
                },

                success: {
                    label: "Salvar",
                    className: "btn-success",
                    callback: function () {
                        return saveReembDias();
                    }
                }
            }
        });
    }


    function loadReembSemanal(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel',action:'manageReembolsoSemanal')}",
            data: "parId=${postoCombustivelInstance?.id}&id=" + rbId,
            success: function (data) {
                openReembSemanal(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });

    }

    function loadReembIntervalo(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel',action:'manageReembolso')}",
            data: "parId=${postoCombustivelInstance?.id}&id=" + rbId,
            success: function (data) {
                openReembIntervalo(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });

    }

    function openReembDias(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel', action: 'manageReembolsoDias')}",
            data: {parId: "${postoCombustivelInstance?.id}", id: rbId},
            success: function (data) {
                openReembDiasDialog(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });

    }

    function openModal(rbId) {

        if (checked != null) {

            if (checked.val() == 'SEMANAL')
                loadReembSemanal(rbId)
            else if (checked.val() == 'INTERVALOS_MULTIPLOS')
                loadReembIntervalo(rbId)
            else if (checked.val() == 'DIAS_TRANSCORRIDOS')
                openReembDias();

        } else {
            alert("Selecione primeiramente um Tipo de Reembolso!");
        }
    }

    function saveReembIntervalos() {
        hasError = false;
        $(".mandatory").each(function () {
            if ($(this).val() == "") {
                addErrorList("Campo [" + $(this).parent().get(0).childNodes[0].innerHTML + "] obrigatório");
                hasError = true;
            }
        });
        if (!hasError) {
            var data = $("#reembolsoForm").serialize();
            $.ajax({
                type: 'POST',
                url: "${createLink(controller:'postoCombustivel',action:'saveReembolso')}",
                data: data,
                success: function (o) {
                    if (o.type == "ok") {
                        rbIntervaloTable.ajax.reload();
                    }
                    else if (o.type == "error")
                        alert(o.message);
                },
                statusCode: {
                    404: function () {
                        alert("Falha ao abrir página para inclusão de Novos Funcionários");
                    }
                }
            });
        } else {
            showErrorList();
        }
    }

    function saveReembSemanal() {
        hasError = false;
        $(".required").each(function () {
            if ($(this).val() == "") {
                addErrorList("Campo [" + $(this).parent().get(0).childNodes[0].innerHTML + "] obrigatório");
                hasError = true;
            }
        });

        if (!hasError) {
            var data = $("#reembolsoSemanalForm").serialize();
            $.ajax({
                type: 'POST',
                url: "${createLink(controller:'postoCombustivel',action:'saveReembolsoSemanal')}",
                data: data,
                success: function (o) {
                    if (o.type == "ok") {
                        alert(o.message);
                        rbSemanalTable.ajax.reload();

                    }
                    else if (o.type == "error")
                        alert(o.message);
                },
                statusCode: {
                    404: function () {
                        alert("Falha ao abrir página para inclusão de Novos Funcionários");
                    }
                }
            });
        } else {
            showErrorList();
        }
    }

    var saveReembDias = function () {

        errorList = [];

        var hasError = false;
        $(".required").each(function () {
            if ($(this).val() == "") {
                addErrorList("Campo <strong>" + $(this).parent().get(0).childNodes[1].innerHTML + "</strong> obrigatório");
                hasError = true;
            }
        });


        if (!hasError) {
            $.post("${createLink(action: 'saveReembolsoDias')}", $("#reembDiasForm").serialize())
                    .done(function (o) {
                        if (o.type == "ok") {
                            alert(o.message);
                            loadReembDias("${postoCombustivelInstance?.id}");
                        }
                        else if (o.type == "error")
                            alert(o.message);

                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        alert(textStatus);
                    })
                    .always(function () {
                        return true;
                    });

        } else {
            showErrorList();
            return false;
        }
    }

    var loadReembDias = function(empId) {
        let link = "${createLink(action: 'loadReembolsoDias')}";
        $.get(link, {id: empId})
            .done(function(gsp) {
                $("#divDias").html(gsp);
            })
            .fail(function(err){
                alert(err);
            });
    }

    var addErrorList = function (err) {
        if (typeof err != 'undefined')
            errorList.push(err);
    }

    var showErrorList = function () {

        let htmlError = "<div class='alert alert-danger' role='alert'>";

        for (let i = 0; i < errorList.length; i++)
            htmlError += "<span class='glyphicon glyphicon-exclamation-sign'></span> " + errorList[i] + "<br>";

        htmlError += "</div>"

        $(".error").html(htmlError);

    }


</script>