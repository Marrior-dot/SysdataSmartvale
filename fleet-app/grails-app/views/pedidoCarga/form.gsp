<%@ page import="com.sysdata.gestaofrota.PedidoCarga" %>
<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create" params="[unidade_id:unidadeInstance?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
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
                
	                <fieldset style="border:1px solid;font-size:14px;">
						<label><span>RH</span>${unidadeInstance?.rh.nome}</label>
						<label><span>Unidade</span>${unidadeInstance?.id}-${unidadeInstance?.nome}</label>
						<div class="clear"></div>
	                </fieldset>
	                
					<fieldset class="uppercase">
						<label><span>Data de Carga</span><gui:datePicker id="dataCarga" name="dataCarga" value="${pedidoCargaInstance?.dataCarga}" formatString="dd/MM/yyyy"/></label>
						<label><span>Taxa Pedido (%)</span>${pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}</label>
					</fieldset>
					
					<fieldset>
						<h2>Categorias de Funcionários</h2>
						<g:render template="categorias"></g:render>					
					</fieldset>

					<fieldset>
						<h2>Funcionários</h2>
						<g:render template="funcionarios"></g:render>					
					</fieldset>

                    <g:if test="${action=='visualizando'}">
                        <table>
                            <tr>
                                <th>Total do Pedido (R$)</th>
                                <th>Taxa do Pedido (R$)</th>
                                <th>Total Geral do Pedido (R$)</th>
                            </tr>
                            <tr>
                                <th>${totalPedido}</th>
                                <th>${totalPedido * (pedidoCargaInstance?.taxa)/100}</th>
                                <th>${pedidoCargaInstance?.total}</th>
                            </tr>
                        </table></br>
                    </g:if>


                   %{-- <fieldset class="uppercase">
                        <label><span>Total do Pedido(R$) </span>${pedidoCargaInstance?.total - pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}</label>
                        <label><span>Taxa do Pedido(R$) </span>${pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}</label>
                        <label><span>Total Geral do Pedido(R$) </span>${pedidoCargaInstance?.total + pedidoCargaInstance?.taxa?:unidadeInstance?.rh.taxaPedido}</label>
                    </fieldset>--}%

				</div>
                <div class="buttons">
					<g:if test="${action=='novo'}">
						<span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
					</g:if>
                	<g:if test="${action=='editando'}">
	                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
	                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                	</g:if>
                	<g:if test="${action=='visualizando' && pedidoCargaInstance?.status==StatusPedidoCarga.NOVO}">
	                	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                	</g:if>
                </div>
            </g:form>
        </div>
    </body>
</html>
