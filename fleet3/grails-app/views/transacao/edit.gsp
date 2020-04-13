

<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${transacaoInstance}">
            <div class="errors">
                <g:renderErrors bean="${transacaoInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${transacaoInstance?.id}" />
                <g:hiddenField name="version" value="${transacaoInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lancamentos"><g:message code="transacao.lancamentos.label" default="Lancamentos" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: transacaoInstance, field: 'lancamentos', 'errors')}">
                                    
<ul>
<g:each in="${transacaoInstance?.lancamentos?}" var="l">
    <li><g:link controller="lancamento" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="lancamento" action="create" params="['transacao.id': transacaoInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'lancamento.label', default: 'Lancamento')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="participante"><g:message code="transacao.participante.label" default="Participante" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: transacaoInstance, field: 'participante', 'errors')}">
                                    <g:select name="participante.id" from="${com.sysdata.gestaofrota.Participante.list()}" optionKey="id" value="${transacaoInstance?.participante?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="transacao.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: transacaoInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${com.sysdata.gestaofrota.StatusTransacao?.values()}" value="${transacaoInstance?.status}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="valor"><g:message code="transacao.valor.label" default="Valor" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: transacaoInstance, field: 'valor', 'errors')}">
                                    <g:textField name="valor" value="${fieldValue(bean: transacaoInstance, field: 'valor')}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
