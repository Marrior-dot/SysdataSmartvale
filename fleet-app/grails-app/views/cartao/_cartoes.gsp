<%@ page import="com.sysdata.gestaofrota.TipoCartao; com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Util" %>


<div class="panel panel-default">
	<div class="panel-heading">
		<button type="button" id="btnCardModal" class="btn btn-default">
			<i class="glyphicon glyphicon-plus"></i>&nbsp;Cartão Provisório</button>
	</div>
	<div class="panel-body">

		<g:set var="currentCard" value="${portador?.cartaoAtivo ?: portador?.cartaoAtual}"></g:set>

		<table class="table" style="font-size: 12px">
			<thead>
			<tr>
				<th>Cartão</th>
				<th>Saldo Total</th>
				<th>Tipo</th>
				<g:if test="${currentCard?.tipo == TipoCartao.PROVISORIO}">
				<th></th>
				</g:if>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td>${currentCard ?: '<< Sem cartão vinculado >>'}</td>
				<td>${Util.formatCurrency(portador?.saldoTotal)}</td>
				<td>${currentCard?.tipo?.name}</td>
				<g:if test="${currentCard?.tipo == TipoCartao.PROVISORIO}">
				<td><button id="btnUnlink" class="btn btn-circle" data-toggle="tooltip"
                            title="Desvincula Cartão do Portador"><span class="glyphicon glyphicon-minus"></span>
                    </button>
                </td>
				</g:if>
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
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="divMessage" hidden>
							<div class="alert alert-danger" role="alert">
								<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;<span id="errorMessage"></span>
                            </div>
						</div>
						<div class="row form-group">
							<div class="col-md-6">
								<label>Cartão Provisório</label>
								<g:textField name="card" class="form-control cartao enable"></g:textField>
							</div>
							<div class="col-md-6">
								<label>Data Limite</label>
								<g:textField name="limitDate" class="form-control datepicker enable"></g:textField>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="btnLink" class="btn btn-primary">Vincular Cartão</button>
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
			</div>
		</div>
	</div>
</div>


<script>
	var tempCardModal = $("#tempCardModal");
    var tabCartoes = $("#tabCartoes");

    function loadCartoesVinculados(portadorId) {
        $.get("${createLink(controller: 'cartao', action: 'findAllCartoesPortador')}", { prtId: portadorId }, function() {

        })
        .done(function(data) {
            tabCartoes.html(data);
        });
    }

    function showErrorMessage(error) {
        var divMessage = tempCardModal.find("#divMessage");
        divMessage.show();
        divMessage.find("#errorMessage").text(" " + error);
        console.log("Erro: ", error);
    }

    function clearErrorMessage() {
        var divMessage = tempCardModal.find("#divMessage");
        divMessage.find("#errorMessage").text("");
        divMessage.hide();
    }

	function linkToCardHolder(cardNumber, cardHolderId, limitDate) {
		$.get("${createLink(controller: 'vinculoCartaoProvisorio', action: 'linkToCardHolder')}",
				{
					cardNumber: cardNumber,
					cardHolderId: cardHolderId,
					limitDate: limitDate
				}
		).done(function(data) {
			console.log(data);
			tempCardModal.modal('hide');
		}).fail(function(xhr) {
            showErrorMessage(xhr.responseText)
		});
	}

	function unlinkToCardHolder(cardId, cardHolderId) {
		$.get("${createLink(controller: 'vinculoCartaoProvisorio', action: 'unlinkFromCardHolder')}",
				{
					cardId: cardId,
					cardHolderId: cardHolderId,
				}
		).done(function(data) {
			console.log(data);
            loadCartoesVinculados(cardHolderId);
		}).fail(function(xhr) {
			console.log("Erro: ", xhr.responseText);
		});
	}

	tempCardModal.find("#btnLink").click(function() {
		var cardNumber = $("input[name='card']").val();
		var limitDate = $("input[name='limitDate']").val();
		var cardHolderId = "${portador.id}";
		linkToCardHolder(cardNumber, cardHolderId, limitDate);
	});

    function initCardModal() {
        var cardInput = tempCardModal.find('#card');
        var limitDateInput = tempCardModal.find('#limitDate');
        cardInput.val('');
        limitDateInput.val('');
        clearErrorMessage();

        tempCardModal.modal('show');
    }

    $("#btnCardModal").click(function() {
        initCardModal();
    });

	$("#btnUnlink").click(function() {
		if (confirm("Confirma a Desvinculação do Cartão Provisório do Portador?")) {
			var cardId = "${currentCard?.id}";
			var cardHolderId = "${portador.id}";
			unlinkToCardHolder(cardId, cardHolderId);
            loadCartoesVinculados(cardHolderId);
		}
	});

    tempCardModal.on('hidden.bs.modal', function (e) {
        var cardHolderId = "${portador.id}";
        loadCartoesVinculados(cardHolderId);
    });

</script>
