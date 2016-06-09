<script type="text/javascript" src="${resource(dir:'js',file:'plugins/bootbox/bootbox.min.js') }"></script>

<style>
    .modal-dialog {
        z-index: 1500;
    }

</style>


<div class="panel panel-default">
    <div class="panel-heading">Funcionários habilitados para condução</div>
    <div class="panel-body">
        <div class="buttons">
            <a href="#" id="incFunc" class="btn btn-default" ><span class="glyphicon glyphicon-plus"></span>Habilitar Funcionário</a>

        </div>

        <span id="message"></span>

        <div class="list">
            <table id="funcVeicTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                <thead>
                    <th>CPF</th>
                    <th>Nome</th>
                    <th>Matrícula</th>
                    <th></th>
                </thead>
            </table>
        </div>
    </div>
</div>



<script type="text/javascript">

    var funcVeicTable

    $(document).ready(function(){

        funcVeicTable=$("#funcVeicTable").DataTable({
            "ajax":{
                "url":"${createLink(controller:'veiculo',action:'listFuncionariosJSON')}",
                "data":{"id":${veiculoInstance?.id}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"cpf"},
                {"data":"nome"},
                {"data":"matricula"},
                {"data":"acao"}
            ]
        });

        $("#incFunc").on("click",function(e){
            e.preventDefault();
            loadFuncModal();
        });


    });

    function removeFuncionario(idFunc){
        $.ajax({
            type:'POST',
            url:"${createLink(controller:'veiculo',action:'removeFuncionario')}",
            data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+idFunc,
            beforeSend:function(data){
                $("#message").html("Removendo relação entre Funcionário e Veículo...");
                $("#message").show();
            },
            success:function(data){
                //Recarrega a lista de funcionários relacionados ao veículo
                $("#message").html(data);
                $("#message").show();
                $("#message").fadeOut(5000);
                funcVeicTable.ajax.reload();
            }

        });
    }



    function bindFuncVeic(){

        funcSel.forEach(function(el){

            $.ajax({
                type:'POST',
                url:"${createLink(controller:'veiculo',action:'addFuncionario')}",
                data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+el,
                beforeSend:function(data){
                    $("#message").html("Incluindo relação entre Funcionário e Veículo...");
                    $("#message").show();
                },
                success:function(data){
                    //Recarrega a lista de funcionários relacionados ao veículo

                    $("#message").html(data);
                    $("#message").show();
                    $("#message").fadeOut(5000);


                    funcVeicTable.ajax.reload();

                }
            });


        });

    }


    function openFuncModal(html) {
        bootbox.dialog({

            title: "Vinculação Funcionário ao Veículo",

            message:html,

            buttons: {
                success: {
                    label: "Habilitar",
                    className: "btn-success",
                    callback: function() {
                        bindFuncVeic();
                    }
                }
            }
        });
    }

    function loadFuncModal() {

        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'funcionario',action:'search')}",
            data: "controller=funcionario&unidade_id=${veiculoInstance?.unidade?.id}&action=filtro",
            success: function (data) {
                openFuncModal(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });
    }


</script>



%{--


<span id="message"></span>

<gui:dialog
        id="searchFuncDialog"
        title="Pesquisa de Funcionários"
        draggable="false"
        width="500px"
        update="searchForm"
        fixedcenter="true"
        close="false"
        modal="true"
        buttons="[
                [text:'Selecionar', handler: 'yesHandler', isDefault: true],
                [text:'Cancelar', handler: 'function() {this.cancel();}', isDefault: false]
        ]"
>
    <div id="searchForm"></div>
</gui:dialog>


<script>
    function openWindow(){
        $.ajax({
            type:'POST',
            url:"${createLink(controller:'funcionario',action:'search')}",
            data:"controller=funcionario&unidade_id=${veiculoInstance?.unidade?.id}&action=filtro",
            success:function(data){
                $("#searchForm").html(data);
                GRAILSUI.searchFuncDialog.show();
            },
            statusCode:{
                404:function(){
                    openMessage('error',"Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });
    }

    function removeFuncionario(idFunc){
        $.ajax({
            type:'POST',
            url:"${createLink(controller:'veiculo',action:'removeFuncionario')}",
            data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+idFunc,
            beforeSend:function(data){
                $("#message").html("Removendo relação entre Funcionário e Veículo...");
                $("#message").show();
            },
            success:function(data){
                //Recarrega a lista de funcionários relacionados ao veículo
                updateDataTable();
                $("#message").html(data);
                $("#message").show();
                $("#message").fadeOut(5000);
            }

        });
    }

    function updateDataTable(){

        var params="id=${veiculoInstance?.id}";

        GRAILSUI.funcionariosDT.customQueryString=params;
        GRAILSUI.funcionariosDT.loadingDialog.show();
        GRAILSUI.funcionariosDT.cleanup();

        var sortedBy = GRAILSUI.funcionariosDT.get('sortedBy');
        var newState = {
            startIndex: 0,
            sorting: {
                key: sortedBy.key,
                dir: ((sortedBy.dir == YAHOO.widget.DataTable.CLASS_DESC) ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC)
            },
            pagination : {
                recordOffset: 0,
                rowsPerPage: GRAILSUI.funcionariosDT.get("paginator").getRowsPerPage()
            }
        };

        var oCallback = {
            success: GRAILSUI.funcionariosDT.onDataReturnInitializeTable,
            failure: GRAILSUI.funcionariosDT.onDataReturnInitializeTable,
            scope: GRAILSUI.funcionariosDT,
            argument: newState
        };

        GRAILSUI.funcionariosDT.getDataSource().sendRequest(GRAILSUI.funcionariosDT.buildQueryString(newState), oCallback);

    }

    var yesHandler=function(o) {

        var selected=GRAILSUI.funcSearchDT.getRecord(GRAILSUI.funcSearchDT.getSelectedRows()[0]);
        selFuncId=selected.getData('id')

        var exists=false;
        if(selFuncId){
            var set=GRAILSUI.funcionariosDT.getRecordSet();
            var rec
            for(i=0;i<set.getLength();i++){
                rec=set.getRecord(i);
                if(rec.getData('id')==selFuncId){
                    exists=true;
                    break;
                }
            }
            if(exists){
                showError("Nova relação inválida! Funcionário já relacionado com veículo");
            }else{
                $.ajax({
                    type:'POST',
                    url:"${createLink(controller:'veiculo',action:'addFuncionario')}",
                    data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+selFuncId,
                    beforeSend:function(data){
                        $("#message").html("Incluindo relação entre Funcionário e Veículo...");
                        $("#message").show();
                    },
                    success:function(data){
                        //Recarrega a lista de funcionários relacionados ao veículo
                        updateDataTable();
                        $("#message").html(data);
                        $("#message").show();
                        $("#message").fadeOut(5000);
                    }
                });
            }
        }else{
            showMessage("Nenhum funcionário foi selecionado!");
        }
        this.cancel();
    }


</script>


--}%


