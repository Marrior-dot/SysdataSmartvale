<%@ page import="com.sysdata.gestaofrota.Util" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br>
        <div class="panel panel-default">
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

                <div class="nav">
                    <div class="buttons">
                        <a class="btn btn-default" href="${createLink(uri: '/')}">
                            <span class="glyphicon glyphicon-home"></span>
                            <g:message code="default.home.label"/>
                        </a>

                        <g:if test="${unidadeInstance}">
                            <g:link class="btn btn-default" action="create" params="${[unidade_id: unidadeInstance?.id]}">
                                <i class="glyphicon glyphicon-plus"></i>
                                <g:message code="default.new.label" args="${[entityName]}"/>
                            </g:link>
                        </g:if>
                    </div>
                    <br><br>
                </div>

                <div class="body">

                    <g:hiddenField name="unidade_id" value="${unidadeInstance?.id}"/>

                    <g:render template="search" model="${[statusPedidoCarga: statusPedidoCarga]}"/>

                    <g:if test="${pedidoCargaInstanceList?.size() > 0}">
                        <table class="table table-striped table-bordered table-hover table-condensed">
                            <thead>
                            <tr>
                                <g:bootstrapSortableColumn property="id" title="Id" sign="_19"/>
                                <g:bootstrapSortableColumn property="unidade" title="Unidade" sign="az"/>
                                <g:bootstrapSortableColumn property="dataPedido" title="Data Pedido"/>
                                <g:bootstrapSortableColumn property="dataCarga" title="Data Carga"/>
                                <g:bootstrapSortableColumn property="total" title="Total" sign="_19"/>
                                <g:bootstrapSortableColumn property="statusSolicitacaoCarga" title="Status" sign="az"/>
                                <g:bootstrapSortableColumn property="acoes" title="Ações"/>
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
                                        <td><g:formatReal value="${pedido?.total}"/></td>
                                        <td>${pedido.status}</td>
                                        <td>
                                            <sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADM">
                                                <g:if test="${pedido.status==com.sysdata.gestaofrota.StatusPedidoCarga.NOVO}">
                                                    <g:link action="liberarPedido" id="${pedido.id}">Liberar</g:link>
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
        </div>
    </body>
</html>
