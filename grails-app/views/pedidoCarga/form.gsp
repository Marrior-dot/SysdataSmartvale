<%@ page import="java.text.SimpleDateFormat; com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
%{--
        <g:javascript library="jquery" plugin="jquery"/>
--}%

        <meta name="layout" content="bootstrap-layout" />
        <gui:resources components="['tabView','dataTable','dialog','autoComplete']"/>
        <link rel="stylesheet" href="${resource(dir:'css',file:'frota.css')}" />
      %{--  <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>--}%

        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'table-javascript-style.css')}" />
        <style>
        #yui-pg1-1-first-span,
        #yui-pg1-1-first-link,
        #yui-pg1-1-prev-span,
        #yui-pg1-1-prev-link,
        #yui-pg1-1-next-span,
        #yui-pg1-1-next-link,
        #yui-pg1-1-last-span,
        #yui-pg1-1-last-link,
        #yui-pg1-0-first-span,
        #yui-pg1-0-first-link,
        #yui-pg1-0-last-span,
        #yui-pg1-0-last-link,
        #yui-pg1-0-next-span,
        #yui-pg1-0-next-link,
        #yui-pg1-0-prev-span,
        #yui-pg1-0-prev-link,
        #yui-pg1-0-page-report,
        #yui-pg1-1-page-report,
        #yui-dt1-paginator0,
        #yui-pg0-1-last-span
        {
            display: none;
        }
        </style>
    </head>
    <body>
    <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>
            <div class="panel-body">
                <div class="nav">
                    <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                    <g:link class="btn btn-default" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
                    <g:link class="btn btn-default" action="create" params="[unidade_id:unidadeInstance?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link>
                </div>
                <div class="body">
                    <br><br>
                    <g:if test="${flash.message}">
                        <div class="alert alert-info">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${pedidoCargaInstance}">
                        <div class="errors">
                            <span style="font-weight:bold;padding-left:10px">Erro ao gerar Pedido de Carga</span>
                            <g:renderErrors bean="${pedidoCargaInstance}" as="list" />
                        </div>
                    </g:hasErrors>
                    <g:form method="post" >
                        <g:hiddenField name="id" value="${pedidoCargaInstance?.id}" />
                        <g:hiddenField name="version" value="${pedidoCargaInstance?.version}" />
                        <g:hiddenField name="action" value="${action}"/>
                        <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
                        <div class="dialog">
                            <br>
                            <div class=" panel panel-default">
                                <div class="panel-body">

                                        <label><span>RH: </span>${unidadeInstance?.rh.nome}</label><br>
                                        <label><span>Unidade: </span>${unidadeInstance?.id}-${unidadeInstance?.nome}</label>
                                        <div class="clear"></div>
                                </div>
                            </div>

                           %{-- <fieldset class="uppercase">
                                <label><span>Data de Carga</span><gui:datePicker id="dataCarga" name="dataCarga" value="${pedidoCargaInstance?.dataCarga}" formatString="dd/MM/yyyy"/></label>
                                <label><span>Taxa Pedido (%)</span>${pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}</label>
                            </fieldset>--}%

                            <fieldset class="uppercase">
                                <label><span>Data de Carga</span><input id="dataCarga" name="dataCarga" class="form-control datepicker" value="${

                                    Util.formattedDate(pedidoCargaInstance?.dataCarga)}"></label>
                                <label><span>Taxa Pedido (%)</span><input class="form-control"  value="${pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}" disabled></label>
                            </fieldset>

                            <fieldset>
                                <h2>Categorias de Funcionários</h2>
                                <g:render template="categorias"></g:render>
                            </fieldset>

                            <fieldset>
                                <h2>Funcionários</h2>
                                <g:render template="funcionarios"></g:render>
                            </fieldset>

                        </div>
                        <br><br>
                        <div class="buttons">
                            <g:if test="${action=='novo'}">
                                <g:actionSubmit class="btn btn-default" action="save" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                            </g:if>
                            <g:if test="${action=='editando'}">
                                <span class="button"><g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                                <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                            </g:if>
                            <g:if test="${action=='visualizando' && pedidoCargaInstance?.status==StatusPedidoCarga.NOVO}">
                                <span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                                <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                            </g:if>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>

    </body>
</html>
