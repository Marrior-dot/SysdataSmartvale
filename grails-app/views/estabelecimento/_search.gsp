<%@ page import="com.sysdata.gestaofrota.Util" %>


<g:if test="${!action || action==Util.ACTION_VIEW}">
    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
        <div class="buttons">
            <g:actionSubmit class="btn btn-default" action="create" value="Criar Estabelecimento"/>
        </div>
    </sec:ifAnyGranted>
</g:if>


<g:form controller="${controller}">

	<br/>
	
	<input type="hidden" name="empId" value="${empId}"/>


	<div class="list">

        <table id="estTable" class="table table-striped table-bordered table-hover table-condensed table-default">

            <thead>
                <th>Cod.Estab</th>
                <th>Razão Social</th>
                <th>Nome Fantasia</th>
                <th></th>
            </thead>

        </table>

    </div>


</g:form>


<script type="text/javascript">

    $(document).ready(function(){

        $("#estTable").DataTable({

            "ajax":{
                "url":"${createLink(controller:'estabelecimento',action:'listAllJSON')}",
                "data":{"empId":"${empId}"},
                "dataSrc":"results"
            },
            "columns":[

                {"data":"razao"},
                {"data":"nomeFantasia"},
                {"data":"codEstab"},
                {"data":"acao"}
            ]
        });
    });

</script>