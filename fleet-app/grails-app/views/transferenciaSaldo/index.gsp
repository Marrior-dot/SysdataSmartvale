<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Transferência de Saldo"/>
    <title>${relatorio}</title>
    <export:resource />

    <style>
        .mensagemCartao {
            padding-top: 2rem;
            color: #985f0d;
        }
    </style>

</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>${relatorio}</h4>
    </div>
    <div class="panel-body">
        <div class="buttons-top ">
            <g:link uri="/" class="btn btn-default">
                <span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/>
            </g:link>
        </div>
        <alert:all/>
        <g:form action="transferir">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-6 form-group ">
                        <label>Nº Cartão Origem</label>
                        <g:textField name="cartaoOrigem" placeholder="Cartão Origem" class="form-control number"
                                        value="${params.cartaoOrigem}"></g:textField>
                    </div>
                    <div class="col-md-6 form-group " >
                        <div class="mensagemCartao">
                            <span id="mensagem-cartaoOrigem" ></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 form-group ">
                        <label>Nº Cartão Destino</label>
                        <g:textField name="cartaoDestino"  placeholder="Cartão Destino" class="form-control number"
                                        value="${params.cartaoDestino}" maxlength="19" ></g:textField>
                    </div>
                    <div class="col-md-6 form-group ">
                        <div class="mensagemCartao">
                            <span id="mensagem-cartaoDestino" ></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 form-group ">
                        <label>Valor da Transferência</label>
                        <g:textField name="valorTransferencia" placeholder="Valor" class="form-control money"
                                     value="${params.valorTransferencia}" maxlength="19"></g:textField>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><i class=""></i> Transferir Saldo</button>
                </div>
            </div>
        </div>
        </g:form>
    </div>
<script>
    $("input[name^='cartao']").blur(function() {
        var inputName = $(this);
        var numeroCartao = $(this).val();
        $.get("${createLink(controller: 'cartao', action: 'findByNumero')}", {cartao: numeroCartao}, function() {

        })
       .done(function(data) {
            $("#mensagem-" + inputName.attr('name')).html(data);
        })
        .fail(function(jqxhr, textStatus, error) {
            $("#mensagem-" + inputName.attr('name')).html(jqxhr.responseText);
        });
    });

</script>
</body>
</html>