<div class="panel panel-default panel-top">
    <div class="panel-heading">Pesquisar</div>

    <div class="panel-body">
        <div class="form-horizontal">
            <g:form action="list" role="search">
                <div class="row">
                    <div class="col-xs-3 input-group-sm">
                        <label class="control-label" for="searchDataPedido">Data Pedido</label>
                        <input name="searchDataPedido" id="searchDataPedido" class="datepicker form-control" value="${searchDataPedido}"/>
                    </div>

                    <div class="col-xs-3 input-group-sm">
                        <label class="control-label" for="searchDataCarga">Data Carga</label>
                        <input id="searchDataCarga" name="searchDataCarga" value="${searchDataCarga}" class="datepicker form-control"/>
                    </div>

                    <g:if test="${!unidadeInstance}">
                        <div class="col-xs-3 input-group-sm">
                            <label class="control-label" for="searchUnidade">Unidade</label>
                            <g:textField class="form-control" id="searchUnidade" name="searchUnidade" value="${searchUnidade}"/>
                        </div>
                    </g:if>

                    <div class="col-xs-3 input-group-sm">
                        <label class="control-label" for="searchStatus">Status</label>
                        <g:select name="searchStatus" value="${searchStatus}"
                                  from="${statusPedidoCarga}" class="form-control"
                                  noSelection="['': 'Todos']"
                                  optionValue="nome"/>
                    </div>
                </div>

                <div style="float: right; margin-top: 2em">
                    <g:actionSubmit name="pesquisar" action="list" class="btn btn-default" value="Pesquisar"/>
                </div>
            </g:form>
        </div>
    </div>
</div>


%{--

<style>

    table#pedCrgTable{
        font-size:10px;
        table-layout:fixed;
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
            "defCampos":[
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
--}%







