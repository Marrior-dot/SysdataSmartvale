<g:form controller="${controller}">
    <br/>

    <div class="panel panel-default">
        <div class="panel-heading">Pesquisa</div>

        <div class="panel-body">
            <div class="list">
                    <table id="userTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                        <thead>
                        <th>Nome</th>
                        <th>Login</th>
                        <th>Organização</th>
                        <th>Permissões</th>
                        </thead>
                    </table>
            </div>
        </div>
    </div>
</g:form>

<script type="text/javascript">

    $(document).ready(function(){

        $("#userTable").DataTable({

            //"serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'user',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id ?: 'null'}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"name"},
                {"data":"login"},
                {"data":"owner"},
                {"data": "roles"},
            ]
        });
    });
</script>

