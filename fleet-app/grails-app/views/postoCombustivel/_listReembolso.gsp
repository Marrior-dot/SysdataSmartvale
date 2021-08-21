<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoIntervalo" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoSemanal" %>
<%@ page import="com.sysdata.gestaofrota.TipoReembolso" %>

<style>
    .modal-dialog {
        z-index: 1500;
    }
</style>

<div class="panel panel-default panel-top">

    <div class="panel-body">

        <div class="row">
            <div class="col-md-3">

                <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                    <g:set var="enableSelect" value="enable"></g:set>
                </sec:ifAnyGranted>

                <div class="form-group">
                    <label for="tipoReembolso">Tipo Reembolso</label>

                    <g:select name="tipoReembolso"
                              class="form-control ${enableSelect}"
                              from="${TipoReembolso.values()}"
                              optionValue="nome"
                              noSelection="${['null': '-- Escolha um --']}"
                              value="${postoCombustivelInstance?.tipoReembolso}"></g:select>

                </div>

            </div>

            <div class="col-md-3">

                <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                    <div id="btnReemb" class="col-md-3" style="padding-top: 1.5em;">
                        <button type="button" class="btn btn-default" onclick="openModal();" >
                            <i class="glyphicon glyphicon-plus"></i>&nbsp;Adicionar Reembolso
                        </button>
                    </div>
                </sec:ifAnyGranted>

            </div>

        </div>

        <div class="row">
            <div class="col-md-12">
                <div id="divIntervalo"></div>
                <div id="divSemanal"></div>
                <div id="divDias"></div>
            </div>
        </div>
    </div>

</div>



<asset:javascript src="plugins/bootbox/bootbox.min.js"></asset:javascript>

<script type="text/javascript">

    var errorList = [];

    $(function () {

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            if (e.target.href.includes("#calendario")) {
                initCalendario();
            }
        });

        $("input[name='tipoReembolso']").change(function () {

            const tipoReemb = $("input[name='tipoReembolso']").val();

            if (tipoReemb === 'INTERVALOS_MULTIPLOS') {
                $("#divIntervalo").show();
                $("#divSemanal").hide();
                $("#divDias").hide();
            } else if (tipoReemb === 'SEMANAL') {
                $("#divIntervalo").hide();
                $("#divDias").hide();
                $("#divSemanal").show();
            } else if (tipoReemb === 'DIAS_FIXOS') {
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

                        const empId = $("#id").val();
                        const tipoReemb = $("#tipoReembolso").val();

                        if (tipoReemb === 'SEMANAL')
                            loadReembSemanal(empId);
                        else if (tipoReemb === 'INTERVALOS_MULTIPLOS')
                            loadReembIntervalo(empId);
                        else if (tipoReemb === 'DIAS_FIXOS')
                            loadReembDias(empId);

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

    function openReembIntervaloDialog(html) {
        bootbox.dialog({
            title: "Intervalos Multiplos",
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
                        return saveReembIntervalos();
                    }
                }
            }
        });
    }

    function openReembDiasDialog(html) {
        bootbox.dialog({
            title: "Dias Fixos",
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

    function openReembSemanalDialog(html) {
        bootbox.dialog({
            title: "Reembolso Semanal",
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
                        return saveReembSemanal();
                    }
                }
            }
        });
    }

    function loadReembSemanal(empId) {

        let link = "${createLink(action: 'loadReembolsoSemanal')}";
        $.get(link, {id: empId}, function(gsp) {
            $("#divIntervalo").hide();
            $("#divDias").hide();
            $("#divSemanal").show();
            $("#divSemanal").html(gsp);
        })
        .fail(function(err){
            //openMessage('error', "Falha ao carregar Reembolso Semanal!");
            console.log("Erro: " + err);
        });
    }

    function loadReembIntervalo(empId) {

        let link = "${createLink(action: 'loadReembolsoIntervalos')}";
        $.get(link, {id: empId}, function(gsp) {
            $("#divIntervalo").show();
            $("#divSemanal").hide();
            $("#divDias").hide();
            $("#divIntervalo").html(gsp);
        })
        .fail(function(err){
            console.log("Erro: " + err);
            alert("Falha ao carregar Intervalos de Reembolso!");
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
                    alert("Falha ao abrir página Reembolso Dias Fixos");
                }
            }
        });
    }

    function openReembSemanal(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel', action: 'manageReembolsoSemanal')}",
            data: {parId: "${postoCombustivelInstance?.id}", id: rbId},
            success: function (data) {
                openReembSemanalDialog(data);
            },
            statusCode: {
                404: function () {
                    alert("Falha ao abrir página Reembolso Semanal!");
                }
            }
        });

    }

    function openReembIntervalos(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel', action: 'manageReembolso')}",
            data: {parId: "${postoCombustivelInstance?.id}", id: rbId},
            success: function (data) {
                openReembIntervaloDialog(data);
            },
            statusCode: {
                404: function () {
                    alert("Falha ao abrir página Reembolso Semanal!");
                }
            }
        });

    }

    function openModal(rbId) {

        const tipoReemb = $("#tipoReembolso").val();

        if (tipoReemb === 'SEMANAL')
            openReembSemanal(rbId)
        else if (tipoReemb === 'INTERVALOS_MULTIPLOS')
            openReembIntervalos(rbId)
        else if (tipoReemb === 'DIAS_FIXOS')
            openReembDias(rbId);

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
                        const empId = $("#id").val();
                        loadReembIntervalo(empId);
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
            return true;
        } else {
            showErrorList();
            return false;
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
                        const empId = $("#id").val();
                        loadReembSemanal(empId);
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
            return true;
        } else {
            showErrorList();
            return false;
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
                            const empId = $("#id").val();
                            loadReembDias(empId);

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
        $.get(link, {id: empId}, function(gsp) {
            $("#divIntervalo").hide();
            $("#divSemanal").hide();
            $("#divDias").show();
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

    const initCalendario = function() {

        const tipoReemb = $("#tipoReembolso").val();
        const empId = $("#id").val();

        if (tipoReemb === 'INTERVALOS_MULTIPLOS') {
            loadReembIntervalo(empId);
        } else if (tipoReemb === 'SEMANAL') {
            loadReembSemanal(empId);
        } else if (tipoReemb === 'DIAS_FIXOS') {
            loadReembDias(empId);
        } else {
            $("#divIntervalo").hide();
            $("#divSemanal").hide();
            $("#divDias").hide();

        }
    }



</script>