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

        <div class="panel panel-default">
            <g:form action="list">
                <div class="panel-heading">
                    Filtros
                </div>
                <div class="panel-body">
            %{--<g:render template="/components/rhUnidadeSelect"></g:render>--}%
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Nº Cartão Origem 2</label>
                            <g:textField name="cartaoOrigem"  placeholder="Cartão Origem" class="form-control number" value="${params.cartaoOrigem}"></g:textField>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Nº Cartão Destino 2</label>
                            <g:textField name="cartaoDestino"  placeholder="Cartão Destino" class="form-control number" value="${params.cartaoDestino}"></g:textField>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Valor da Transferência 2</label>
                            <g:textField name="valorTransferencia"  placeholder="Valor" class="form-control money" value="${params.valorTransferencia}"></g:textField>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-default" ><i class=""></i> Transferir Saldo</button>
                </div>
            </g:form>
        </div>
        </div>
    </div>
</body>
</html>