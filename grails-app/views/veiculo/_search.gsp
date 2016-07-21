<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Veiculo" %>



<div class="panel panel-default">

    <div class="panel-heading">Lista de Veículos</div>

    <div class="panel-body">

        <g:if test="${!action || action==Util.ACTION_VIEW}">
            <div class="buttons">
                <g:link class="btn btn-default" controller="veiculo" action="${unidade_id?'create':'selectRhUnidade'}">
                    <span class="glyphicon glyphicon-plus"></span> ${message(code:'default.new.label', args:[message(code:'veiculo')]) }
                </g:link>
            </div>
        </g:if>

        <g:form controller="${controller}">
            <div class="list">
                <table id="veicTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                        <th>Placa</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Ano</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>




<script type="text/javascript">

    $(document).ready(function(){

        $("#veicTable").DataTable({

            //"serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'veiculo',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"placa"},
                {"data":"marca"},
                {"data":"modelo"},
                {"data":"ano"}
            ]
        });
    });
</script>
