<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.MaquinaMotorizada" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <g:set var="relatorio" value="Relatório Histórico de Transações Frota "/>
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
                        <div class="col-md-2">
                            <label class="control-label" for="placa">Placa</label>
                            <g:textField name="placa" class="form-control placa" value="${params.placa}"></g:textField>

                        </div>
                    <div class="col-md-2">
                        <label class="control-label" for="codEquipamento">Cod Equipamento</label>
                        <g:textField name="codigo" class="form-control equipamento" value="${params.codigo}"></g:textField>
                    </div>

                        <div class="col-md-2">
                            <label class="control-label" for="dataInicio">Data Inicial</label>
                            <g:textField name="dataInicio" class="form-control datepicker" value="${params.dataInicio}"></g:textField>
                        </div>
                        <div class="col-md-2">
                            <label class="control-label" for="dataFim">Data Final</label>
                            <g:textField name="dataFim" class="form-control datepicker" value="${params.dataFim}"></g:textField>
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
            <th>Nsu</th>
            <th>Data</th>
            <th>Hora</th>
            <th>Terminal</th>
            <th>Estabelecimento</th>
            <th>CNPJ</th>
            <th>Placa / Cod Equipamento</th>
            <th>Veiculo / Equipamento</th>
            <th>Cartão</th>
            <th>Funcionário</th>
            <th>Matricula</th>
            <th>Produto</th>
            <th>Hodômetro</th>
            <th>Preço Litro</th>
            <th>Valor</th>
            <th>Saldo</th>
            <th>Qtd Litros</th>
            <th>Cliente</th>
            <th>Unidade</th>
            <th>Tipo</th>
            <th>Status</th>
            </thead>
            <tbody>

          <g:each in="${historicoFrotaList}" var="cveic">
                <tr>
                        <td>${cveic.nsu}</td>
                        <td><g:formatDate date="${cveic?.dateCreated}" format="dd/MM/yyyy" /></td>
                        <td><g:formatDate date="${cveic?.dateCreated}" format="HH:mm:ss" />
                         <td>${cveic.terminal}</td>
                        <td>${cveic?.estabelecimento?.nomeFantasia}</td>
                        <td>${cveic?.estabelecimento?.cnpj}</td>
                        <g:if test="${cveic.maquina?.instanceOf(Veiculo) && cveic.maquina?.placa}">
                        <td>${cveic.maquina?.placa}</td>
                        </g:if>
                        <g:if test="${cveic.maquina?.instanceOf(com.sysdata.gestaofrota.Equipamento) && cveic.maquina?.codigo}">
                         <td>${cveic.maquina?.codigo}</td>
                        </g:if>
                        <g:if test="${cveic.maquina?.instanceOf(Veiculo) && cveic.maquina?.placa}">
                        <td>${cveic.maquina.marca} / ${cveic.maquina.modelo} </td>
                        </g:if>
                        <g:elseif test="${cveic.maquina?.instanceOf(com.sysdata.gestaofrota.Equipamento) && cveic.maquina?.codigo}">
                        <td>${cveic.maquina.tipo}</td>
                        </g:elseif>
                        <td>${cveic?.numeroCartao}</td>
                        <td>${cveic.participante?.nome}</td>
                        <td>${cveic.participante?.matricula}</td>
                        <td>${cveic?.produtos*.produto?.nome}</td>
                        <g:if test="${cveic.maquina?.instanceOf(Veiculo) && cveic.maquina?.placa}">
                        <td>${cveic.quilometragem}</td>
                         </g:if>
                         <g:elseif test="${cveic.tipo.nome == com.sysdata.gestaofrota.TipoTransacao.SERVICOS}">
                        <td>   </td>
                        </g:elseif>
                         <g:else> <td>  </td></g:else>
                        <td>${cveic?.precoUnitario}</td>
                        <td>${cveic?.valor}</td>
                        <td>${cveic?.cartao.saldoTotal}</td>
                        <td>${cveic?.qtd_litros}</td>
                        <td>${cveic?.cartao?.portador?.unidade?.rh?.nomeFantasia}</td>
                        <td>${cveic?.cartao?.portador?.unidade?.nome}</td>
                        <td>${cveic?.tipo.nome}</td>
                        <td>${cveic?.statusControle.nome}</td>
                </tr>
            </g:each>

            </tbody>
            <tfoot>

            </tfoot>
        </table>
        </div>

        <g:paginate total="${historicoFrotaCount}" params="${params}"/>

        <export:formats formats="['csv', 'excel', 'pdf']" params="${params}"/>
    </div>
</div>
</body>
</html>