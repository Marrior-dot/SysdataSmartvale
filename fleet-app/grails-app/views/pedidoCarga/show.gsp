<%@ page import="com.sysdata.gestaofrota.StatusProgramacao; com.sysdata.gestaofrota.PedidoCargaProgramado; com.sysdata.gestaofrota.PedidoCargaInstancia; com.sysdata.gestaofrota.Util; com.sysdata.gestaofrota.StatusPedidoCarga" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<br/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.create.label" args="[entityName]"/></h4>
    </div>

    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>

            <g:link class="btn btn-default" action="list">
                <i class="glyphicon glyphicon-th-list"></i>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>

            <g:link class="btn btn-default" action="create" params="${[unidade_id: pedidoCargaInstance?.unidade?.id]}">
                <i class="glyphicon glyphicon-plus"></i>
                <g:message code="default.new.label" args="${[entityName]}"/>
            </g:link>

            <g:link class="btn btn-default" action="gerarPlanilha" params="${[id: pedidoCargaInstance.id]}">
                <i class="glyphicon glyphicon-paperclip"></i>
                Gerar Planilha
            </g:link>
        </div>

        <br/>

        <g:if test="${flash.message}">
            <div class="alert alert-info">${flash.message}</div>
        </g:if>

        <g:if test="${flash.errors}">
            <div class="alert alert-danger">${flash.errors}</div>
        </g:if>


        <g:form id="${pedidoCargaInstance.id}" method="POST">

            <g:render template="form" model="${[pedidoCargaInstance: pedidoCargaInstance, action: Util.ACTION_VIEW]}"/>

            <g:if test="${(pedidoCargaInstance.instanceOf(PedidoCargaInstancia) && pedidoCargaInstance?.status == StatusPedidoCarga.NOVO) ||
                    (pedidoCargaInstance.instanceOf(PedidoCargaProgramado) && pedidoCargaInstance?.statusProgramacao == StatusProgramacao.AGENDADO)
            }">
%{--                <g:actionSubmit action="edit" class="btn btn-default" value="${message(code: 'default.button.edit.label', default: 'Editar')}"/>--}%

                <button type="submit" class="btn btn-danger"
                        name="_action_delete"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Você tem certeza?')}');">
                    <i class="glyphicon glyphicon-trash"></i>
                    <g:message code="default.button.delete.label" ></g:message>
                </button>


            </g:if>
        </g:form>

    </div>
</div>

</body>
</html>