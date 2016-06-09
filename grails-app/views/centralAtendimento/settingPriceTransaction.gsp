<%@ page import="com.sysdata.gestaofrota.TipoCombustivel" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="bootstrap-layout"/>
		
		
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

		<div class="body">
			<br><br>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4>					Configuração de Preços de Combustível
					</h4>
				</div>
				<div class="panel-body">
					<a class="btn btn-default" href="${createLink(uri: '/')}">
						<span class="glyphicon glyphicon-home"></span>
						<g:message code="default.home.label"/>
					</a>
					<br><br>
					<g:if test="${flash.message}">
						<div class="alert alert-info">${flash.message}</div>
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
							<label><span>Estabelecimento</span><g:textField class="numeric form-control" size="15" name="estabelecimento" value="${estabelecimento}"></g:textField> <span class="inline" id="razaoSocial"></span></label>
							<div class="clear"></div>
							<label><span>Combustível</span><g:select class="form-control" name="combustivel" from="${TipoCombustivel.asList()}" optionValue="nome"></g:select></label>
							<div class="clear"></div>
							<label><span>Preço</span><g:textField name="preco" value="${preco}" class="form-control"></g:textField></label>
							<div class="clear"></div>
						</fieldset>
						<div class="buttons">
							<span class="button"><g:submitButton class="btn btn-default" name="doSettingPriceTransaction" value="Solicitar" /></span>
						</div>
					</g:form>
				</div>
				</div>
			</div>

	</body>
	
	
</html>
