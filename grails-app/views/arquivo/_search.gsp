<%@ page import="com.sysdata.gestaofrota.TipoArquivo" %>

<fieldset class="search">
	<g:form controller="${controller}">
		<div class="list">
			<table id="arqTable" class="table table-striped table-bordered table-hover table-condensed table-default">
				<thead>
				<th>DATA/HORA</th>
				<th>TIPO</th>
				<th>NOME DO ARQUIVO	</th>
				<th>STATUS</th>
				</thead>
			</table>
		</div>
	</g:form>
</fieldset>

<script>
	$(document).ready(function(){

		$("#arqTable").DataTable({

			//"serverSide": true,
			"ajax":{
				"url":"${createLink(controller:'arquivo',action:'listAllJSON')}",
				"data":{"unidade_id":${unidade_id ?: 'null'}},
				"dataSrc":"results"
			},
			"columns":[
				{"data":"date"},
				{"data":"tipo"},
				{"data":"nome"},
				{"data":"status"}
			]
		});
	});
</script>


