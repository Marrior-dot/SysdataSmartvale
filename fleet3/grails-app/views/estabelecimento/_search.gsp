<%@ page import="com.sysdata.gestaofrota.Util" %>


<g:form controller="${controller}" action="create">
    <input type="hidden" name="empId" value="${empId}"/>

    <g:if test="${!action || action == Util.ACTION_VIEW}">
        <br/>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
            <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-plus"></i>&nbsp;Criar Estabelecimento</button>
        </sec:ifAnyGranted>
    </g:if>


    <div class="panel-top">
        <table id="estTable" class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
            <th>Cod.Estab</th>
            <th>Razão Social</th>
            <th>Nome Fantasia</th>
            </thead>
        </table>
    </div>
</g:form>


<script type="text/javascript">
    $(document).ready(function () {
        $("#estTable").DataTable({
            "ajax": {
                "url": "${createLink(controller:'estabelecimento',action:'listAllJSON')}",
                "data": {"empId": "${empId}"},
                "dataSrc": "results"
            },
            "columns": [

                {"data": "codEstab"},
                {"data": "razao"},
                {"data": "nomeFantasia"},
            ]
        });
    });
</script>