<div id="divFuncionarios">
    <g:set var="entityName" value="Funcionários"></g:set>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]"/></h4>
        </div>

        <div class="buttons-top">
            <g:link class="btn btn-default" controller="funcionario" action="create" params="[unidade_id: unidadeInstance?.id]">
                <i class="glyphicon glyphicon-plus"></i> Novo Funcionário(a)</g:link>
        </div>


        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>


            <g:hiddenField name="unidadeId" value="${unidadeInstance?.id}"></g:hiddenField>

            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-3">
                            <label>CPF</label>
                            <g:textField name="cpf" class="form-control cpf"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label>Nome</label>
                            <g:textField name="nomeFuncionario" class="form-control"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label>Matrícula</label>
                            <g:textField name="matricula" class="form-control"></g:textField>
                        </div>
                    </div>
                </div>
            </div>

            <div id="funcList">
                <g:render template="/funcionario/tableFuncList" model="[funcionariosList: funcionariosList, funcionariosCount: funcionariosCount]"></g:render>
            </div>
        </div>
    </div>
</div>

<script>

    var funcSel = [];

    function doRequest(params) {

        $.get("${createLink(controller: 'funcionario', action: 'listByUnidade')}", params, function(data) {
        })
                .done(function(data){
                    $("#funcList").html(data);
                })
                .fail(function(xhr, status, err) {
                    console.error(xhr.responseText);
                });

    }

    $("input[name=nomeFuncionario]").keyup(function() {
        var params = {
            unidId: $("#unidadeId").val(),
            cpf: $("#cpf").val(),
            nome: $(this).val(),
            matricula: $("#matricula").val()
        };
        doRequest(params);
    });

    $("input[name=cpf]").keyup(function() {
        var params = {
            unidId: $("#unidadeId").val(),
            cpf: $(this).val(),
            nome: $("#nomeFuncionario").val(),
            matricula: $("#matricula").val()
        };
        doRequest(params);
    });


    $("input[name=matricula]").keyup(function() {
        var params = {
            unidId: $("#unidadeId").val(),
            cpf: $("#cpf").val(),
            nome: $("#nomeFuncionario").val(),
            matricula: $(this).val()
        };
        doRequest(params);
    });

    // Pega evento de click nos links de paginação
    $("#divFuncionarios").on('click', 'a.step', function() {
        event.preventDefault();

        var link = $(this).attr("href");
        var aux = link.substring(link.lastIndexOf("offset"), link.length);
        var offsetParam = aux.substring(0, aux.indexOf("&") > -1 ? aux.indexOf("&") : aux.length);
        var offset = offsetParam.split("=")[1];
        var params = {
            unidId: $("#unidadeId").val(),
            cpf: $("#cpf").val(),
            nome: $("#nomeFuncionario").val(),
            matricula: $(this).val(),
            offset: offset
        };
        doRequest(params);
    });

    // Seleção de um funcionário na tabela
    $("#divFuncionarios").on("click", '#tableFunc tbody tr', function () {
        $(this).toggleClass("selected");
        var tr = $(this).closest('tr');
        var funcId = tr.data('id');
        if ($(this).hasClass("selected"))
            funcSel.push(funcId);
        else
            funcSel.pop(funcId);
    });
</script>


