<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Equipamento" %>


<div class="panel panel-default">
    <div class="panel-heading">Lista de Equipamentos</div>
    <div class="panel-body">
        <g:if test="${!action || action==Util.ACTION_VIEW}">
            <div class="buttons">
                <g:link class="btn btn-default" action="${unidade_id?'create':'selectRhUnidade'}">
                    <span class="glyphicon glyphicon-plus"></span>${message(code:'default.new.label', args:[message(code:'equipamento.label')]) }
                </g:link>
            </div>
        </g:if>

        <g:form controller="${controller}">
            <div class="list">
                <table id="equipTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                        <th>Código</th>
                        <th>Descrição</th>
                        <th>Tipo</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function(){

        $("#equipTable").DataTable({

            //"serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'equipamento',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"codigo"},
                {"data":"descricao"},
                {"data":"tipo"}
            ]
        });
    });
</script>