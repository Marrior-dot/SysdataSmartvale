<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga; com.sysdata.gestaofrota.Util" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">

                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>

                <g:if test="${flash.errors}">
                    <div class="alert alert-danger">${flash.errors}</div>
                </g:if>

                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>

                <g:link class="btn btn-default" action="create">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="${[entityName]}"/>
                </g:link>


                <g:render template="search" model="${[statusPedidoCarga: statusPedidoCarga]}"/>

                <g:if test="${pedidoCargaInstanceList?.size() > 0}">
                    <table class="table table-striped table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Unidade</th>
                            <th>Data Pedido</th>
                            <th>Data Carga</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th class="text-center">Ações</th>
                        </tr>
                        </thead>
                        <tbody>
                            <g:each in="${pedidoCargaInstanceList}" var="pedido">
                                <tr>
                                    <td>
                                        <g:link controller="pedidoCarga" action="show" id="${pedido.id}">
                                            ${pedido.id}
                                        </g:link>
                                    </td>
                                    <td>${pedido?.unidade?.nome}</td>
                                    <td><g:formatDate date="${pedido?.dateCreated}" format="dd/MM/yyyy"/></td>
                                    <td><g:formatDate date="${pedido?.dataCarga}" format="dd/MM/yyyy"/></td>
                                    <td><g:formatNumber number="${pedido?.total}" type="currency"/></td>
                                    <td>${pedido.status.nome}</td>
                                    <td class="text-center">
                                        <sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADMIN">
                                            <g:if test="${pedido.status == StatusPedidoCarga.NOVO}">
                                                <g:link class="btn btn-primary" action="liberarPedido" id="${pedido.id}" title="Liberar">
                                                    <i class="glyphicon glyphicon-share"></i>
                                                </g:link>
                                                <g:link class="btn btn-danger" action="cancelarPedido" id="${pedido.id}" title="Cancelar">
                                                    <i class="glyphicon glyphicon-remove"></i>
                                                </g:link>

                                            </g:if>
                                        </sec:ifAnyGranted>
                                    </td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>

                    <div>
                        <g:paginate class="pagination" total="${pedidoCargaInstanceCount ?: 0}"/>
                    </div>

                </g:if>
                <g:else>
                    <div class="well text-center">SEM DADOS</div>
                </g:else>
            </div>
        </div>
    </body>
</html>
