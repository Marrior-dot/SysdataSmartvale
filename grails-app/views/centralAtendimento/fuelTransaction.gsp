<%@ page import="com.sysdata.gestaofrota.TipoCombustivel" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		<jq:jquery>

			$("input[name='matricula']").focusout(function(){
				matricula=$(this).val();
				$.ajax({
					type:'POST',
					url:"${createLink(controller:'funcionario',action:'getByMatricula')}",
					data:"matricula="+matricula,
					success:function(o){
						if(o==null){
							$("#nome").attr("style","color:red");
							$("#nome").html("Não foi encontrado Funcionário com este código");
						}
						else {
							$("#nome").attr("style","color:blue");
							$("#nome").html(o);
						}
					}
				});
			})

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
							$("#razaoSocial").html(o);
						}
					}
				});
			})
			
			
			$("input[name='placa']").focusout(function(){
				placa=$(this).val();
				$.ajax({
					type:'POST',
					url:"${createLink(controller:'veiculo',action:'getByPlaca')}",
					data:"placa="+placa,
					success:function(o){
						if(o==null){
							$("#veiculo").attr("style","color:red");
							$("#veiculo").html("Não foi encontrado Veículo com esta placa");
						}
						else {
							$("#veiculo").attr("style","color:blue");
							$("#veiculo").html(o);
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
            
            <g:form method="post" >
            	<g:hiddenField name="nsuTerminal" value="${commandInstance?.nsuTerminal}"/>
            	<g:hiddenField name="nsuHost" value="${commandInstance?.nsuHost}"/>
            	<g:hiddenField name="dataHost" value="${commandInstance?.dataHost}"/>
            	<g:hiddenField name="horaHost" value="${commandInstance?.horaHost}"/>
            
				<fieldset>
					<label><span>Cartão</span><g:textField class="numeric" size="19" name="cartao" value="${commandInstance?.cartao}"></g:textField></label>
					<div class="clear"></div>
					<label><span>Vencimento</span><g:textField class="numeric" size="4" name="vencimento" value="${commandInstance?.vencimento}"></g:textField></label>
					<div class="clear"></div>				
					<label><span>Matrícula Motorista</span><g:textField class="numeric" size="12" name="matricula" value="${commandInstance?.matricula}"></g:textField> <span class="inline" id="nome"></span></label>
					<div class="clear"></div>
					<label><span>Placa Veículo</span><g:textField class="placa" name="placa" value="${commandInstance?.placa}"></g:textField> <span class="inline" id="veiculo"></span></label>
					<div class="clear"></div>
					<label><span>Quilometragem</span><g:textField class="numeric" size="9" name="quilometragem" value="${commandInstance?.quilometragem}"></g:textField></label>
					<div class="clear"></div>
					<label><span>Estabelecimento</span><g:textField class="numeric" size="15" name="estabelecimento" value="${commandInstance?.estabelecimento}"></g:textField> <span class="inline" id="razaoSocial"></span></label>
					<div class="clear"></div>
					<label><span>Combustível</span><g:select name="tipoCombustivel" from="${TipoCombustivel.values()}" optionValue="nome" value="commandInstance?.tipoCombustivel"></g:select></label>
					<div class="clear"></div>
					<label><span>Valor Transação</span><g:textField name="valor" value="${commandInstance?.valor}" class="currency"></g:textField></label>
					<div class="clear"></div>
					<label><span>Senha</span><g:passwordField name="senha" ></g:passwordField></label>
					<div class="clear"></div>
					
					
				</fieldset>
	            <div class="buttons">
	            	<g:if test="${commandInstance?.autorizada}">
		                <span class="button"><g:actionSubmit action="confirmFuelTransaction" value="Confirmar" /></span>
		                <span class="button"><g:actionSubmit action="confirmFuelTransaction" value="Desfazer" /></span>
	            	</g:if>
	            	<g:else>
		                <span class="button"><g:actionSubmit action="doFuelTransaction" value="Solicitar" /></span>
	            	</g:else>
	            </div>
            </g:form>
		</div>
	</body>
	
	
</html>
