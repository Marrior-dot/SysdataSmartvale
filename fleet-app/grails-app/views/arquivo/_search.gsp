<%@ page import="com.sysdata.gestaofrota.TipoArquivo" %>

<g:form controller="${controller}">
	<div class="list">
		<table id="arqTable" class="table table-striped table-bordered table-hover table-condensed table-default">
			<thead>
			<th>ID</th>
			<th>Data/Hora</th>
			<th>Tipo</th>
			<th>Nome</th>
			<th>Status</th>
			<th>Ações</th>
			</thead>
		</table>
	</div>
</g:form>

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
				{"data":"id"},
				{"data":"data"},
				{"data":"tipo"},
				{"data":"nome"},
				{"data":"status"},
				{"data":"acao"}
			],
			"order": [[ 0, "desc" ]]
		});
	});
</script>


