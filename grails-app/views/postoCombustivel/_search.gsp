<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.PostoCombustivel" %>

		<g:form controller="${controller}">
			<div class="list">
				<table id="funcTable"
					   class="table table-striped table-bordered table-hover table-condensed table-default">
					<thead>
						<th>CNPJ</th>
						<th>Razão Social</th>
						<th>Nome Fantasia</th>
					</thead>
				</table>
			</div>
		</g:form>


<script type="text/javascript">

	var funcSel=[];

	$(document).ready(function () {


		$("#funcTable").DataTable({
			//"serverSide": true,
			"ajax": {
				"url": "${createLink(controller:'postoCombustivel',action:'listAllJSON')}",
				"dataSrc": "results"
			},
			"columns": [
				{"data": "cnpj"},
				{"data": "razao"},
				{"data": "nomeFantasia"}
			]
		});

		// Seleção de um funcionário na tabela

		$("#funcTable tbody").on("click",'tr',function(){

			var fid=$(this).find("td:first").html();
			if($(this).hasClass("selected")){
				$(this).removeClass("selected");
				funcSel.pop(fid);
			}
			else{
				$(this).addClass("selected");
				funcSel.push(fid);
			}
		});

	});
</script>

