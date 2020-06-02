/**
 * Created by hyago on 10/08/17.
 */

/**
 * Essa função cria uma submissão AJAX ao formulário disponibilizado e
 * deve ser chamada no onready da página. Desabilita todos os inputs do
 * tipo submit da página e coloca uma animação (carregando) dentro do
 * botão de submit do formulário que a chamou.
 *
 * ATENÇÃO: alertas do tipo success e error são criados dentro do formulário
 * para serem renderizados de acordo com a resposta do servidor.
 *
 * @param {DOM Form} form formulário que será enviado via AJAX
 * @param {int delay} tempo de espera (em segundos) antes da submissão ser enviada. defaults 1
 */
function submitFormByAjax(form, delay, successCallback, erroCallback, resetForm, dataType) {
    if (form === undefined || form.length === 0) {
        console.error("Formulário enviado a função 'submitFormByAjax' não encontrado.");
        return;
    }

    var delay = delay !== undefined ? delay * 1000 : 1000;
    var succesAlertComponent = form.find("div.alert-success");
    var erroAlertComponent = form.find("div.alert-danger");

    if (resetForm === undefined) resetForm = false;
    if (dataType === undefined) dataType = 'json';

    var criarAlertContainer = function () {
        var alertsContainer = $('<div id="alertsContainer" class="col-md-12"></div>');
        form.prepend(alertsContainer);

        return alertsContainer;
    };

    if (succesAlertComponent.length === 0) {
        succesAlertComponent = $('<div class="alert alert-success" role="alert"></div>');
        criarAlertContainer().prepend(succesAlertComponent);
    }
    if (erroAlertComponent.length === 0) {
        var alertsContainer = form.find("div#alertsContainer");
        if (alertsContainer.length == 0) var alertsContainer = criarAlertContainer();

        erroAlertComponent = $('<div class="alert alert-danger" role="alert"></div>');
        alertsContainer.prepend(erroAlertComponent);
    }

    succesAlertComponent.hide();
    erroAlertComponent.hide();

    form.submit(function (event) {
        event.preventDefault();
        succesAlertComponent.hide();
        erroAlertComponent.hide();
        disableSubmits();

        var url = form.attr('action');
        var method = form.attr('method');

        if(dataType === 'json') url += '.json';

        if (url == undefined) {
            console.error("Formulário não contem endereço.");
            return;
        }

        var callAJAX = function () {
            $.ajax({
                type: method !== undefined ? method : "POST",
                url: url,
                dataType: dataType,
                data: form.serialize(),

                success: function (data) {
                    if (successCallback && typeof successCallback === "function") {
                        successCallback(data, form);
                    }

                    if (resetForm) form[0].reset();
                    if (data) {
                        if (dataType === 'json' && data.msg && data.msg.length > 0) {
                            succesAlertComponent.html(data.msg);
                            succesAlertComponent.show();
                        }
                        else if (dataType === 'html' && data.responseText > 0) {
                            succesAlertComponent.html(data);
                            succesAlertComponent.show();
                        }
                    }
                },
                error: function (request) {
                    //console.error(request);
                    if (dataType === 'json' && request.responseJSON && request.responseJSON.erro &&
                        request.responseJSON.erro.length > 0) {
                        erroAlertComponent.html(request.responseJSON.erro);
                        erroAlertComponent.show();
                    }
                    else if(dataType === 'html' && request.responseText.length > 0){
                        if(request.statusText === "Internal Server Error")
                            erroAlertComponent.html("Erro interno no Servidor");
                        else erroAlertComponent.html(request.responseText);
                        erroAlertComponent.show();
                    }
                    if (erroCallback && typeof erroCallback === "function") {
                        erroCallback(request, form);
                    }
                },
                complete: function () {
                    enableSubmits();
                }
            });
        };

        setTimeout(function () {
            callAJAX();
        }, delay);
    });
}

function disableSubmits() {
    var submits = $('button:submit,input:submit');
    submits.append($('<i class="fa fa-cog fa-spin fa-fw"></i>'));
    submits.attr('disabled', 'disabled');
}

function enableSubmits() {
    var submits = $('button:submit,input:submit');
    submits.removeAttr('disabled');
    submits.find("i.fa-spin").remove();
}