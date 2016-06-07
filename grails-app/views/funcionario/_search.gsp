<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>


<div class="panel panel-default">

    <div class="panel-heading">Lista de Funcionários</div>

    <div class="panel-body">

        <g:if test="${!action || action == Util.ACTION_VIEW}">
            <div class="buttons">
                <g:link class="btn btn-default" action="${unidade_id ? 'create' : 'selectRhUnidade'}">
                    <span class="glyphicon glyphicon-plus"></span>${g.message(code: 'default.new.label', args: [message(code: 'funcionario.label', default: 'Funcionario')])}
                </g:link>
            </div>
        </g:if>

        <g:form controller="${controller}">
            <div class="list">
                <table id="funcTable"
                       class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                    <th>CPF</th>
                    <th>Nome</th>
                    <th>Matrícula</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function () {

        $("#funcTable").DataTable({
            //"serverSide": true,
            "ajax": {
                "url": "${createLink(controller:'funcionario',action:'listAllJSON')}",
                "data": {"unidade_id": ${unidade_id ?: 'null'} },
                "dataSrc": "results"
            },
            "columns": [
                {"data": "cpf"},
                {"data": "nome"},
                {"data": "matricula"}
            ]
        });
    });
</script>



