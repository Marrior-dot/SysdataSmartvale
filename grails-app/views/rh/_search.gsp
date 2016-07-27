<g:form controller="${controller}">
	<div class="list">
		<table id="rhsTable"
			   class="table table-striped table-bordered table-hover table-condensed table-default">
			<thead>
			<th>Razão Social</th>
			<th>Nome Fantasia</th>
			<th>CNPJ</th>
			</thead>
		</table>
	</div>
</g:form>

<script type="text/javascript">

	var funcSel=[];

	$(document).ready(function () {

		$("#rhsTable").DataTable({
			//"serverSide": true,
			"ajax": {
				"url": "${createLink(controller:'rh',action:'listAllJSON')}",
				"data": {"unidade_id": ${unidade_id ?: 'null'} },
				"dataSrc": "results"
			},
			"columns": [
				{"data":"razao"},
				{"data": "fantasia"},
				{"data": "cnpj"}
			]
		});

	});
</script>



