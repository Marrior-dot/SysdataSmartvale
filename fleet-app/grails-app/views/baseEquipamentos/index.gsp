<%@ page import="com.sysdata.gestaofrota.Equipamento" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório de Base de Equipamentos"/>
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

        <g:form action="index">
            <div class="panel panel-default panel-top">
                <div class="panel-heading">Pesquisa</div>

                <div class="panel-body">
                    <g:render template="/components/rhUnidadeSelect"></g:render>
                <div class="row">
                        <div class="col-md-3">
                            <label class="control-label" for="codigo">Codigo do Equipamendo</label>
                            <g:textField name="codigo" class="form-control codigo" value="${params.codigo}"></g:textField>
                        </div>

                    </div>

                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
                </div>
            </div>
        </g:form>

        <div class="table-responsive">

        <table class="table table-bordered table-stripped">
            <thead>
            <th>Cliente</th>
            <th>Unidade</th>
            <th>Código</th>
            <th>Tipo</th>
            <th>Media de Consumo</th>
            <th>Cap Tanque(lts)</th>
            <th>Tipo Comb</th>
            <th>Descrição</th>

            </thead>
            <tbody>


            <g:each in="${baseEquipamentosList}" var="equip">
                <tr>
                    <td>${equip.unidade.rh.nomeFantasia}</td>
                    <td>${equip.unidade.nome}</td>
                    <td>${equip.codigo}</td>
                    <td>${equip.tipo} </td>
                    <td>${equip.mediaConsumo}</td>
                    <td>${equip?.capacidadeTanque}</td>
                    <td>${equip?.tipoAbastecimento}</td>
                    <td>${equip.descricao}</td>

                </tr>
            </g:each>

            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${baseEquipamentosCount}" />

        <export:formats formats="['csv', 'excel', 'pdf']" />
    </div>
</div>
</body>
</html>