<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Pesquisar Cartões</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.errors}">
	            <div class="errors">
	            	<ul>
	            		<g:each var="err" in="${flash.errors}">
		            		<li>${err}</li>
	            		</g:each>
	            	</ul>
	            </div>
	        </g:if>
			<g:form method="post" action="${act}">
				<g:hiddenField name="goTo" value="${goTo}"/>
				
				<fieldset>
					<label><span>Cartão p/ Transferir: </span><g:textField name="cartaoParaTransferir"></g:textField></label>
				</fieldset>
                <fieldset>
                    <label><span>Cartão p/ Receber Saldo: </span><g:textField name="cartaoParaReceber"></g:textField></label>
                </fieldset>
                <div class="buttons">
                    <span class="button"><g:submitButton name="${act}" class="forward" value="Prosseguir" /></span>
                </div>
			</g:form>
        </div>
    </body>
</html>


