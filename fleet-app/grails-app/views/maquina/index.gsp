<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.Veiculo" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Listagem de Veículos/Equipamentos</title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>Listagem de Veículos/Equipamentos</h4>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-heading">
                Filtro
            </div>
            <g:form action="index">
            <div class="panel-body">
                <div class="row">
                        <div id="placa" class="col-md-4">
                            <label>Placa</label>
                            <g:textField name="placa" class="form-control placa" value="${params.placa}"></g:textField>
                        </div>
                        <div id="codigo" class="col-md-4">
                            <label>Codígo</label>
                            <g:textField name="codigoEquipamento" class="form-control" value="${params.codigoEquipamento}"></g:textField>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <div class="row">
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-search"></i> Pesquisar</button>
                    </div>
                </div>
            </div>
            </g:form>
            <table class="table">
            <thead>
            <th>Veículo/Equipamento</th>
            <th>Cliente</th>
            <th>Unidade</th>
            </thead>
            <tbody>
            <g:each in="${maquinaList}" var="maq">
                <tr>
                    <g:if test="${maq.instanceOf(Veiculo)}">
                        <td><g:link controller="veiculo" action="show" id="${maq.id}">${maq.identificacaoCompacta}</g:link></td>
                    </g:if>
                    <g:if test="${maq.instanceOf(Equipamento)}">
                        <td><g:link controller="equipamento" action="show" id="${maq.id}">${maq.identificacaoCompacta}</g:link></td>
                    </g:if>
                    <td>${maq.unidade.rh.nome}</td>
                    <td>${maq.unidade.nome}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
            <g:paginate total="${maquinaCount}" params="${params}"></g:paginate>
        </div>


    </div>
</div>

<script>
    $("#tipoMaquina").change(function() {
        alert($(this).val());
    });
</script>

</body>
</html>