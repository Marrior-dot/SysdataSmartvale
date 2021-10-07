<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <title>Processamentos</title>
</head>
<body>

<div class="panel panel-default panel-top">

    <div class="panel-heading">
        <h4>Processamentos</h4>
    </div>

    <div class="panel-body">

        <g:if test="${flash.message}">
            <div class="alert alert-info" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger" role="status">${flash.error}</div>
        </g:if>

        <p>
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            <g:link class="btn btn-default" action="executeAll"><span class="glyphicon glyphicon-play-circle"></span> Executar Todos</g:link>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                Parâmetros
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-3">
                        <label for="dataReferencia">Data de Processamento</label>
                        <g:form action="execute">
                            <g:hiddenField name="id"></g:hiddenField>
                            <g:textField name="dataProcessamento" class="form-control datepicker" value="${params.dataProcessamento}"></g:textField>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>


        <g:if test="${processingList}">
            <table class="table table-bordered table-striped">
                <thead>
                <th>Ordem</th>
                <th>Processamento</th>
                <th>Serviço</th>
                <th>Opções</th>
                </thead>
                <tbody>
                <g:each in="${processingList}" var="p">
                    <tr>
                        <td>${p.order}</td>
                        <td>${p.name}</td>
                        <td>${p.service}</td>
                        <td class="text-center">
                            <button class="btn btn-sm btn-primary" onclick="executarProcessamento(${p.id})">
                                <i class="glyphicon glyphicon-play"></i></button>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="well text-center">Não há Processamentos</div>
        </g:else>
    </div>
</div>

<script>
    function executarProcessamento(procId) {
        $("#id").val(procId);
        $("form").submit();
    }
</script>

</body>
</html>