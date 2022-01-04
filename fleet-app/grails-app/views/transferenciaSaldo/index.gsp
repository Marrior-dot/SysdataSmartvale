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
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>${relatorio}</h4>
    </div>
    <div class="panel-body">

        <g:link uri="/" class="btn btn-default">
            <span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/>
        </g:link>

        <alert:all/>
        <g:form action="transferir">
        <div class="panel panel-default">
            <div class="panel-body">

                <div class="row">
                    <div class="col-md-6">
                        <label>Nº Cartão Origem</label>
                        <g:textField name="cartaoOrigem" placeholder="Cartão Origem" class="form-control number" value="${params.cartaoOrigem}"></g:textField>
                    </div>
                    <div class="col-md-6">
                        <span id="saldoCartao"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <label>Nº Cartão Destino</label>
                        <g:textField name="cartaoDestino"  placeholder="Cartão Destino" class="form-control number" value="${params.cartaoDestino}"></g:textField>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <label>Valor da Transferência</label>
                        <g:textField name="valorTransferencia"  placeholder="Valor" class="form-control money" value="${params.valorTransferencia}"></g:textField>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-default" ><i class=""></i> Transferir Saldo</button>
                </div>
            </div>
        </div>
        </g:form>
    </div>
<script>
    $("#cartaoOrigem").blur(function() {
        var numeroCartao = $(this).val();
        $.getJSON("${createLink(controller: 'cartao', action: 'findByNumero')}", {cartao: numeroCartao}, function() {

        })
       .done(function(data) {
            console.log("Saldo: " + data);
            $("#saldoCartao").html(data);
        })
        .fail(function(jqxhr, textStatus, error) {
            var err = textStatus + ", " + error;
            console.log( "Request Failed: " + err );
        });
    });

</script>
</body>
</html>