
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        
        <style type="text/css">
        
        	table#tbTrans {
    			padding:0;
			    border-collapse: collapse;
			}
			
        	table#tbTrans tr{
    			border:0;
			}
			
        	table#tbTrans td.name {
        		background-color:#48802c;
        		text-align:right;
        		font-weight:bold;
        		color:#FFFFFF;
        	}
        </style>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table id="tbTrans">
                    <tbody>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.id.label" default="Id" /></td>
                            <td valign="top" class="value">${fieldValue(bean: transacaoInstance, field: "id")}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.dateCreated.label" default="Date Created" /></td>
                            <td valign="top" class="value"><g:formatDate date="${transacaoInstance?.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.tipo.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.tipo?.nome}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.status.label" default="Status" /></td>
                            <td valign="top" class="value">${transacaoInstance?.status?.nome}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.statusControle.label" default="Status" /></td>
                            <td valign="top" class="value">${transacaoInstance?.statusControle?.nome}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.valor.label" default="Valor" /></td>
                            <td valign="top" class="value"><g:formatNumber number="${transacaoInstance.valor}" format="#0.00" /></td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.participante.label" default="Participante" /></td>
                            <td valign="top" class="value">${transacaoInstance.participante?.nome}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.cartao.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.numeroCartao}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.codigoEstabelecimento.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.codigoEstabelecimento}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.terminal.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.terminal}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.nsuTerminal.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.nsuTerminal}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.nsu.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.nsu}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.codigoRetorno.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.codigoRetorno}</td>
                        </tr>
                        
						<g:if test="${transacaoInstance.maquina?.instanceOf(com.sysdata.gestaofrota.Veiculo)  }">
						
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="transacao.veiculo.label" default="" /></td>
	                            <td valign="top" class="value">${transacaoInstance.maquina?.placa}</td>
	                        </tr>
	                        
						</g:if>
						
						<g:if test="${transacaoInstance.maquina?.instanceOf(com.sysdata.gestaofrota.Equipamento)  }">
						
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="transacao.equipamento.label" default="Equipamento" /></td>
	                            <td valign="top" class="value">${transacaoInstance.maquina?.codigo}</td>
	                        </tr>
						
						</g:if>
						
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.combustivel.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.combustivel?.nome}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.quilometragem.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.quilometragem}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="transacao.motivoNegacao.label" default="" /></td>
                            <td valign="top" class="value">${transacaoInstance.motivoNegacao?.codigo}-${transacaoInstance.motivoNegacao?.descricao}</td>
                        </tr>
                    
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
