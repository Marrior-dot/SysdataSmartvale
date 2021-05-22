function showModal(divElem, type, message, callback) {

    var html = "<div class='modal fade' id='userMessageModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>"+
        "    <div class='modal-dialog' role='document'>" +
        "        <div class='modal-content'>" +
        "            <div class='modal-header'>" +
        "                <h5 class='modal-title' id='modalTitle'></h5>" +
        "                <button type='button' class='close' data-dismiss='modal' aria-label='Close'>" +
        "                <span aria-hidden='true'>&times;</span>" +
        "                </button>" +
        "            </div>" +
        "            <div class='modal-body'>" +
        "                <span id='message'></span>" +
        "            </div>" +
        "            <div class='modal-footer'>" +
        "                <div id='questionButtons' hidden>" +
        "                    <button type='button' class='btn btn-secondary' data-dismiss='modal'>Não</button>" +
        "                    <button id='yesButton' type='button' class='btn btn-primary' data-dismiss='modal'>Sim</button>" +
        "                </div>" +
        "                <div id='messageButtons' hidden>" +
        "                    <button type='button' class='btn btn-primary' data-dismiss='modal'>OK</button>" +
        "                </div>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";

    divElem.html(html);
    var userModal = $("#userMessageModal");
    if (type === "question") {
        userModal.find("#modalTitle").text("Pergunta");
        userModal.find("#questionButtons").show();
        userModal.find("#messageButtons").hide();
    } else {
        userModal.find("#modalTitle").text("Mensagem");
        userModal.find("#questionButtons").hide();
        userModal.find("#messageButtons").show();
    }
    userModal.find("#message").text(message);
    userModal.modal();
    // Attach event
    $("#yesButton").on('click', callback);
}


