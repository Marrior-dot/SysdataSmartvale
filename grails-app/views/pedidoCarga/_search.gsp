
<div class="list">

    <table id="pedCrgTable" class="table table-striped table-bordered table-hover table-condensed">
        <thead>
            <th>ID</th>
            <th>RH</th>
            <th>Unidade</th>
            <th>Data Pedido</th>
            <th>Data Carga</th>
            <th>Total</th>
            <th>Status</th>
            <th></th>
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








