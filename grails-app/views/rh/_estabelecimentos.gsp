<g:hiddenField name="prgId" value="${rhInstance?.id}"/>

<div class="buttons">
	<span class="button">
		<input type="button" id="btnSaveEst" class="btn btn-default" value="Salvar"/>
	</span>
	
</div>
<br><br>


<table id="estTable" class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
	<thead>
	<th><input type="checkbox" class="enable" id="selAll"></th>
	<th>Razão Social</th>
	<th>Nome Fantasia</th>
	<th>CNPJ</th>
	</thead>
	<tbody>
	<g:each in="${com.sysdata.gestaofrota.PostoCombustivel.list()}" status="i" var="posto">
		<tr>
			<td><input type="checkbox" class="enable"></td>
			<td>${posto.nome}</td>
			<td>${posto.nomeFantasia}</td>
			<td>${posto.cnpj}</td>
		</tr>
	</g:each>
	</tbody>
</table>



<script type="text/javascript">

	$(document).ready(function(){

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
