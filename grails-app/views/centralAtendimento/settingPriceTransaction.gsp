<%@ page import="com.sysdata.gestaofrota.TipoCombustivel" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		
		
	<jq:jquery>
		$("input[name='estabelecimento']").focusout(function(){
			codEstab=$(this).val();
			$.ajax({
				type:'POST',
				url:"${createLink(controller:'estabelecimento',action:'getByCodigo')}",
				data:"codigo="+codEstab,
				success:function(o){
					if(o.razao==null){
						$("#razaoSocial").attr("style","color:red");
						$("#razaoSocial").html("Não foi encontrado Estabelecimento com este código");
					}
					else {
						$("#razaoSocial").attr("style","color:blue");
						$("#razaoSocial").html(o.razao);
					}
				}
			});
		})
	</jq:jquery>
		
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
            
            <g:form method="post" action="doSettingPriceTransaction">
				<fieldset>
					<label><span>Estabelecimento</span><g:textField class="numeric" size="15" name="estabelecimento" value="${estabelecimento}"></g:textField> <span class="inline" id="razaoSocial"></span></label>
					<div class="clear"></div>
					<label><span>Combustível</span><g:select name="combustivel" from="${TipoCombustivel.asList()}" optionValue="nome"></g:select></label>
					<div class="clear"></div>
					<label><span>Preço</span><g:textField name="preco" value="${preco}" class="currency"></g:textField></label>
					<div class="clear"></div>
				</fieldset>
	            <div class="buttons">
	                <span class="button"><g:submitButton name="doSettingPriceTransaction" value="Solicitar" /></span>
	            </div>
            </g:form>
		</div>
	</body>
	
	
</html>
