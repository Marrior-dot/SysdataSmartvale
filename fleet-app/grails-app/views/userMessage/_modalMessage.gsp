<!-- Modal -->
<div class="modal fade" id="userMessageModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <span id="message"></span>
            </div>
            <div class="modal-footer">
                <div id="questionButtons" hidden>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Não</button>
                    <button type="button" class="btn btn-primary">Sim</button>
                </div>
                <div id="messageButtons" hidden>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    function showUserMessage(type, message, callback) {
        var userMessageModal = $('#userMessageModal');
        if (type === "question") {
            userMessageModal.find("#modalTitle").html("Pergunta");
            userMessageModal.find("#questionButtons").show();
            userMessageModal.find("#messageButtons").hide();
        } else if (type === "message") {
            userMessageModal.find("#modalTitle").html("Mensagem");
            userMessageModal.find("#questionButtons").hide();
            userMessageModal.find("#messageButtons").show();
        } else
            throw "Tipo de Mensagem de Usuário não informada!";

        if (message !== undefined && message != null && message.length !== 0) {
            userMessageModal.find("#message").html(message);
            userMessageModal.modal(options);
            userMessageModal.on('', callback);
        } else
            throw "Mensagem não informada para a Caixa de Mensagem";

    }
</script>