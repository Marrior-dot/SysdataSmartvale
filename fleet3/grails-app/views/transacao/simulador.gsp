<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<%@ page import="com.sysdata.gestaofrota.CategoriaFuncionario" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'autorizacao.label', default: 'Autorização')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1>Simulador de Transações</h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.errors}">
	            <div class="errors">
	                <ul>
	                	<g:each in="${flash.errors}" var="err">
	                		<li>${err}</li>
	                	</g:each>
	                </ul>
	            </div>
			</g:if>            
            <g:form method="post" >
                <div class="dialog">
                
					<fieldset class="uppercase">
						<p>
							<label><span>Combustível</span>
								<g:select name="combustivel" 
												value="${transacaoInstance?.combustivel}" 
												noSelection="${['null':'Selecione o combustível...'] }" 
												from="${['Gasolina','Álcool','Diesel'] }">
								</g:select> 
							</label>
						</p>
						<p><label><span>Estabelecimento</span><g:textField name="codigoEstabelecimento" value="${transacaoInstance?.codigoEstabelecimento}" size="15" maxlength="15"/></label></p>
						<p><label><span>Placa</span><g:textField name="placa" value="${transacaoInstance?.placa}" size="8" maxlength="8"/></label></p>
						<p><label><span>Cartão</span><g:textField name="numeroCartao" value="${transacaoInstance?.numeroCartao}" class="numeric" size="19" maxlength="19"/></label></p>
						<p><label><span>Quilometragem</span><g:textField name="quilometragem" value="${transacaoInstance?.quilometragem}" class="numeric" size="6" maxlength="6"/></label></p>
						<p><label><span>Preço Unitário</span><g:textField name="precoUnitario" class="currency" value="${Util.formatCurrency(transacaoInstance?.precoUnitario)}" size="10" maxlength="10"/></label></p>
						<p><label><span>Valor</span><g:textField name="valor" value="${Util.formatCurrency(transacaoInstance?.valor)}" class="currency" size="10" maxlength="10"/></label></p>
						<p><label><span>Senha</span><g:passwordField name="senha" class="numeric" size="4" maxlength="4"/></label></p>
					</fieldset>
				</div>
				
                <div class="buttons">
					<span class="button"><g:actionSubmit class="save" action="autorizar" value="Autorizar" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
