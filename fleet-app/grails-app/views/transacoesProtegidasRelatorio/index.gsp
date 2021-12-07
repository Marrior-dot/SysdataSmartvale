<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Transações Protegidas"/>
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
                    <div class="row">
                        <div class="col-md-3">
                            <label>Data Inicial</label>
                            <g:textField name="dataInicial" class="form-control datepicker" value="${params.dataInicial}"></g:textField>
                        </div>
                        <div class="col-md-3">
                            <label>Data Final</label>
                            <g:textField name="dataFinal" class="form-control datepicker" value="${params.dataFinal}"></g:textField>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-default" ><i class=""></i> Filtrar</button>
                </div>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>