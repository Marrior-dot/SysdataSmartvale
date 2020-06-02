<g:form controller="${controller}">
    <div class="list">
        <table id="cardsTable" class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
            <th>Nº</th>
            <th>Portador</th>
            <th>Data Validade</th>
            <th>Status</th>
            </thead>
        </table>
    </div>
</g:form>

<script type="text/javascript">

    $(document).ready(function () {

        $("#cardsTable").DataTable({
            //"serverSide": true,
            "ajax": {
                "url": "${createLink(action:'listAllJSON')}",
                "dataSrc": "results"
            },
            "columns": [
                {"data": "numero"},
                {"data": "portador"},
                {"data": "validade"},
                {"data": "status"}
            ]
        });

    });
</script>



