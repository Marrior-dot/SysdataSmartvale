
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.show.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="nav">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>
                <g:link class="btn btn-default" action="list">
                    <span class="glyphicon glyphicon-list"></span>
                    <g:message code="default.list.label" args="[entityName]" />
                </g:link>
                <g:link class="btn btn-default" action="listPendentes">
                    <span class="glyphicon glyphicon-list"></span>
                    Lista de Transações Pendentes
                </g:link>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>

                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <th>#</th>
                        <td>${transacaoInstance.id}</td>
                    </tr>
                    <tr>
                        <th>Data/Hora Criação</th>
                        <td><g:formatDate date="${transacaoInstance?.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                    </tr>
                    <tr>
                        <th>Tipo</th>
                        <td>${transacaoInstance.tipo?.nome}</td>
                    </tr>
                    <tr>
                        <th>Status Processamento</th>
                        <td>${transacaoInstance?.status?.nome}</td>
                    </tr>
                    <tr>
                        <th>Status Rede</th>
                        <td>${transacaoInstance?.statusControle?.nome}</td>
                    </tr>
                    <tr>
                        <th>Valor</th>
                        <td><g:formatNumber number="${transacaoInstance.valor}" type="currency" currencySymbol="R\$ " format="#0.00" /></td>
                    </tr>
                    <tr>
                        <th>Funcionário</th>
                        <td>${transacaoInstance.participante?.nome}</td>
                    </tr>
                    <tr>
                        <th>Cartão</th>
                        <td>${transacaoInstance.cartao?.numero}</td>
                    </tr>
                    <tr>
                        <th>Estabelecimento</th>
                        <td>${transacaoInstance.estabelecimento?.nome}</td>
                    </tr>
                    <tr>
                        <th>Terminal</th>
                        <td>${transacaoInstance.terminal}</td>
                    </tr>
                    <tr>
                        <th>NSU Terminal</th>
                        <td>${transacaoInstance.nsuTerminal}</td>
                    </tr>
                    <tr>
                        <th>NSU Autorizador</th>
                        <td>${transacaoInstance.nsu}</td>
                    </tr>
                    <tr>
                        <th>Código Retorno</th>
                        <td>${transacaoInstance.codigoRetorno}</td>
                    </tr>
                    <tr>
                        <th>Motivo Negação</th>
                        <td>${transacaoInstance.motivoNegacao}</td>
                    </tr>
                    <tr>
                        <th>Veículo</th>
                        <td>${transacaoInstance.maquina?.placa}</td>
                    </tr>
                    <tr>
                        <th>Combustível</th>
                        <td>${transacaoInstance.combustivel?.nome}</td>
                    </tr>
                    <tr>
                        <th>Hodômetro</th>
                        <td>${transacaoInstance.quilometragem}</td>
                    </tr>



                    </tbody>
                </table>

            </div>
        </div>
    </div>

    </body>
</html>
