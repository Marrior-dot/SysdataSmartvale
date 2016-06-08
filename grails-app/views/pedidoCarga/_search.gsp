<style>

    table#pedCrgTable{
        font-size:10px;
%{--        table-layout:fixed; --}%
        white-space:nowrap;
        overflow:hidden;
    }

    table .tr{

        line-height:25px;

    }

    .tdId{
        width:5%;
    }

    .tdRh{
        width:25%;
    }
    .tdUn{
        width:25%;
    }

    .tdDp{
        width:5%;
    }

    .tdDc{
        width:5%;
    }

    .tdTt{
        width:5%;
    }

    .tdSt{
        width: 5%;
    }

    .tdAc{
        width:5%;
    }


</style>


<div class="list">

    <table id="pedCrgTable" class="table table-striped table-bordered table-hover table-condensed table-default" >
        <thead>
            <th class="tdId">ID</th>
            <th class="tdRh">RH</th>
            <th class="tdUn">Unidade</th>
            <th class="tdDp">Dt.Ped</th>
            <th class="tdDc">Dt.Carga</th>
            <th class="tdTt">Total</th>
            <th class="tdSt">Status</th>
            <th class="tdAc"></th>
        </thead>

    </table>


</div>

<script type="text/javascript">

    $(document).ready(function(){

        $("#pedCrgTable").DataTable({

            //"serverSide": true,

            "order":[[0,'desc']],

            "ajax":{
                "url":"${createLink(controller:'pedidoCarga',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id?:'null'}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"id"},
                {"data":"rh"},
                {"data":"unidade"},
                {"data":"dataPedido"},
                {"data":"dataCarga"},
                {"data":"total"},
                {"data":"status"},
                {"data":"acoes"}
            ]
        });
    });
</script>








