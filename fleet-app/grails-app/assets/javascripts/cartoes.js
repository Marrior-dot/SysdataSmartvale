var tempCardModal = $("#tempCardModal");
var tabCartoes = $("#tabCartoes");

function loadCartoesVinculados(portadorId) {
    $.get("../../cartao/findAllCartoesPortador", { prtId: portadorId }, function() {

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
    $.get("../../vinculoCartaoProvisoriolinkToCardHolder",
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
    $.get("../../vinculoCartaoProvisorio/unlinkFromCardHolder",
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
