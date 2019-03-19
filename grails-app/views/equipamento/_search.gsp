<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Equipamento" %>

<div class="panel panel-default">
    <div class="panel-heading"><h4>Lista de Equipamentos</h4></div>
    <div class="panel-body">
        <g:if test="${!action || action==Util.ACTION_VIEW}">
            <div class="buttons">
               <g:link class="btn btn-default" controller="equipamento" action="create" params="['unidade.id': unidade_id]">
                    <span class="glyphicon glyphicon-plus"></span>
                    ${message(code:'default.new.label', args:[message(code:'equipamento')]) }
                </g:link>
            </div>
        </g:if>
        <br>
        <g:form controller="${controller}">
            <div class="list">
                <table id="equipTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                        <th>Código</th>
                        <th>Descrição</th>
                        <th>Tipo</th>
                        <th>Cartão</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>

<script type="text/javascript">
    var equipSel = [];

    $(document).ready(function() {
        // Setup - add a text input to each footer cell
        $('#equipTable thead th').each( function () {
            var title = $(this).text();
            $(this).html( '<input type="text" placeholder="Buscar '+title+'" />' );
        } );

        // DataTable
        var table =
                $("#equipTable").DataTable({
                    //"serverSide": true,
                    "ajax":{
                        "url":"${createLink(controller:'equipamento',action:'listAllJSON')}",
                        "data":{"unidade_id":${unidade_id ?: 'null'}},
                        "dataSrc":"results"
                    },
                    "columns":[
                        {"data":"codigo"},
                        {"data":"descricao"},
                        {"data":"tipo"},
                        {"data":"cartao"}
                    ]
                });

        // Seleção de um funcionário na tabela
        $("#equipTable tbody").on("click", 'tr', function () {
            var fid = $(this).find("td:first").html();
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                equipSel.pop(fid);
            }
            else {
                $(this).addClass("selected");
                equipSel.push(fid);
            }
        });

        // Apply the search
        table.columns().every( function () {
            var that = this;

            $( 'input', this.header() ).on( 'keyup change', function () {
                if ( that.search() !== this.value ) {
                    that
                            .search( this.value )
                            .draw();
                }
            } );
        } );

        $('#equipTable_filter').hide()
    } );

    $(document).ready(function () {
    });
</script>
%{--
<script type="text/javascript">

    $(document).ready(function(){
        console.log('${unidade_id}');

        $("#equipTable").DataTable({
            //"serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'equipamento',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id ?: 'null'}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"codigo"},
                {"data":"descricao"},
                {"data":"tipo"},
                {"data":"cartao"}
            ]
        });
    });
</script>--}%
