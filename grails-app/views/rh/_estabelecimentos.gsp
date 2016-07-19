<g:hiddenField name="rhId" value="${rhInstance?.id}"/>

<div class="buttons">
	<span class="button">
		<input type="button" id="btnSaveEst" class="btn btn-default" value="Salvar"/>
	</span>
</div>
<br><br>


<table id="estTable" class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
	<thead>
		<th><input type="checkbox" class="enable" id="selAll"></th>
        <th>CNPJ</th>
        <th>Razão Social</th>
        <th>Nome Fantasia</th>
	</thead>
</table>


<script type="text/javascript">

	$(document).ready(function(){

		$("#estTable").DataTable({

            "ajax":{
                "url":"${createLink(controller:'postoCombustivel',action:'listAllJSON')}",
				"data":"prgId="+$("#rhId").val(),
                "dataSrc":"results"
            },
            "columns":[
                {"data":"sel"},
                {"data":"cnpj"},
                {"data":"razaoSocial"},
                {"data":"nomeFantasia"}
            ]
        });


		$("#btnSaveEst").click(function(){

			$.ajax({
				type:'POST',
				url:"${createLink(action:'saveEstabs')}",
				data:"prgId="+$("input[name='prgId']}").val(),

				success:function(o){
					if(o.type=="error")
						alert(o.message);
					else
						alert("Empresas salvas")
				},
				statusCode:{
					404:function(){
						alert("Falhar ao tentar conexão ao servidor");
					}
				}
			})
		});

		$("#selAll").click(function(){

            if($(this).is(":checked")){

                $("#estTable > tbody > tr").each(function(ind){


                });
            }
        });


	});

</script>
