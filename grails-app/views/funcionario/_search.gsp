<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>


<div class="panel panel-default">

    <div class="panel-heading">Lista de Funcionários</div>

    <div class="panel-body">

        <g:if test="${!action || action == Util.ACTION_VIEW}">
            <div class="buttons">
               <g:link class="btn btn-default" controller="funcionario" action="${unidade_id ? 'create' : 'selectRhUnidade'}" params="[unidade_id:unidade_id]">
                    <span class="glyphicon glyphicon-plus"></span>${g.message(code: 'default.new.label', args: [message(code: 'funcionario.label', default: 'Funcionario')])}
                </g:link>
            </div>
        </g:if>

        <g:form controller="${controller}">
            <div class="list">
                <table id="funcTable"
                       class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                    <th>ID</th>
                    <th>CPF</th>
                    <th>Nome</th>
                    <th>Matrícula</th>
                    <th>Cartão</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>

<script type="text/javascript">

    var funcSel=[];

    $(document).ready(function () {

        $("#funcTable").DataTable({
            //"serverSide": true,
            "ajax": {
                "url": "${createLink(controller:'funcionario',action:'listAllJSON')}",
                "data": {"unidade_id": ${unidade_id ?: 'null'} },
                "dataSrc": "results"
            },
            "columns": [
                {"data": "id"},
                {"data": "cpf"},
                {"data": "nome"},
                {"data": "matricula"},
                {"data": "cartao"}
            ]
        });

        // Seleção de um funcionário na tabela

        $("#funcTable tbody").on("click",'tr',function(){

            var fid=$(this).find("td:first").html();
            if($(this).hasClass("selected")){
                $(this).removeClass("selected");
                funcSel.pop(fid);
            }
            else{
                $(this).addClass("selected");
                funcSel.push(fid);
            }
        });

    });
</script>



