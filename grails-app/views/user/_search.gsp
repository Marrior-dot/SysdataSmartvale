<div class="panel panel-default">

    <div class="panel-body">
        <g:form controller="${controller}">

            <div class="list">
                <table id="usuTable"
                       class="table table-striped table-bordered table-hover table-condensed table-default">
                    <thead>
                        <th>Login</th>
                        <th>Nome</th>
                        <th>Organização</th>
                        <th>Papéis</th>
                    </thead>
                </table>

            </div>
        </g:form>
    </div>
</div>

<script type="text/javascript">


    $(document).ready(function () {

        $("#usuTable").DataTable({
            //"serverSide": true,
            "ajax": {
                "url": "${createLink(controller:'user',action:'listAllJSON')}",
                "dataSrc": "results"
            },
            "columns": [
                {"data": "login"},
                {"data": "name"},
                {"data": "owner"},
                {"data": "roles"}
            ]
        });


    });
</script>
