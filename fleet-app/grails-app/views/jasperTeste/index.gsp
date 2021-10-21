<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Jasper Teste"/>
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

    </div>
</div>
</body>
</html>