<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Util" %>


<div class="panel panel-default">
	<div class="panel-heading">
		<button type="button" class="btn btn-default">Vincular Cartão Provisório</button>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" style="font-size: 12px">
			<thead>
			<tr>
				<th>Cartão</th>
				<th>Saldo Total</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td>${portador?.cartaoAtual}</td>
				<td>${Util.formatCurrency(portador?.saldoTotal)}</td>
			</tr>
			</tbody>
		</table>
	</div>
</div>


<!-- Modal Vínculo Cartão Provisório -->

<div class="modal fade" id="tempCardModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Vínculo Cartão Provisório</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div id="responseQueryPayment"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary">Vincular Cartão</button>
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
			</div>
		</div>
	</div>
</div>


<script>

/*
	function consultarRepasseApi(pagtoLoteId) {
		$("#responseQueryPayment").html("");
		waitingDialog.show();
		$.get("${createLink(action: 'queryPayment')}", {id: pagtoLoteId})
				.done(function(data) {
					$("#responseQueryPayment").html(data);
				})
				.fail(function() {
					alert("Erro ao conectar servidor!");
				})
				.always(function() {
					waitingDialog.hide();
				});
	}

	$('#tempCardModal').on('shown.bs.modal', function (e) {
		consultarRepasseApi("${pagamentoLote.id}");
	})
*/

</script>
