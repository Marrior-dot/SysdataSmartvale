<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Veiculo" %>



<div class="panel panel-default">

    <div class="panel-heading"><h4>Lista de Veículos</h4></div>

    <div class="panel-body">
        <g:if test="${!action || action == Util.ACTION_VIEW}">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">
                <div class="buttons">
                    <g:link class="btn btn-default" controller="veiculo" action="${unidade_id ? 'create' : 'selectRhUnidade'}" params="[unidade_id:unidade_id]">
                        <span class="glyphicon glyphicon-plus"></span>${g.message(code: 'default.new.label', args: [message(code: 'veiculo.label', default: 'Veiculo')])}
                    </g:link>
                </div>
            </sec:ifAnyGranted>
        </g:if>
        <br>
        <g:form controller="${controller}">
            <div class="list">
                <table id="veicTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                        <th>Placa</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Ano</th>
                        <th>Cartão</th>
                    </thead>
                </table>
            </div>
        </g:form>
    </div>
</div>


<style>
thead input {
    width: 100%;
    padding: 3px;
    box-sizing: border-box;
}
</style>


<script type="text/javascript">
    var veicSel = [];
    $(document).ready(function() {
        // Setup - add a text input to each footer cell
        $('#veicTable thead th').each( function () {
            var title = $(this).text();
            $(this).html( '<input type="text" placeholder="Buscar '+title+'" />' );
        } );

        // DataTable
        var table =  $("#veicTable").DataTable({

            //"serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'veiculo',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id ?: 'null'}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"placa"},
                {"data":"marca"},
                {"data":"modelo"},
                {"data":"ano"},
                {"data":"cartao"}
            ]
        });

        // Seleção de um funcionário na tabela
        $("#veicTable tbody").on("click", 'tr', function () {
            var fid = $(this).find("td:first").html();
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                veicSel.pop(fid);
            }
            else {
                $(this).addClass("selected");
                veicSel.push(fid);
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

        $('#veicTable_filter').hide()
    } );
</script>
