<%@ page import="com.sysdata.gestaofrota.TipoCombustivel" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="bootstrap-layout"/>
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
		<br/>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:if test="${flash.errors}">
			<div class="alert alert-danger" role="alert">
				<g:each var="err" in="${flash.errors}">
					<strong>${err}</strong>
				</g:each>
			</div>
		</g:if>

		<div class="body">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4>Transação de Combustível</h4>
				</div>
				<div class="panel-body">
					<div class="buttons">
						<a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
					</div>
					<br/>

					<g:form method="post" >
						<g:hiddenField name="nsuTerminal" value="${commandInstance?.nsuTerminal}"/>
						<g:hiddenField name="nsuHost" value="${commandInstance?.nsuHost}"/>
						<g:hiddenField name="dataHost" value="${commandInstance?.dataHost}"/>
						<g:hiddenField name="horaHost" value="${commandInstance?.horaHost}"/>

						<div class="row">
							<div class="col-md-3">
								<bs:formField id="cartao" name="cartao" value="${commandInstance?.cartao}" label="Cartão" class="col-md-3 only-numbers" maxlength="19"/>
							</div>
							<div class="col-md-3">
								<bs:formField id="vencimento" name="vencimento" value="${commandInstance?.vencimento}" label="Vencimento" class="only-numbers" maxlength="4"/>
							</div>
							<div class="col-md-3">
								<bs:formField id="matricula" name="matricula" value="${commandInstance?.matricula}" label="Matrícula Motorista" class="only-numbers" maxlength="12"/>
							</div>

							<div class="col-md-3">
								<bs:formField id="placa" name="placa" value="${commandInstance?.placa}" label="Placa Veículo" class="placa"/>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<bs:formField id="quilometragem" name="quilometragem" value="${commandInstance?.quilometragem}" label="Quilometragem" class="only-numbers" maxlength="9"/>
							</div>
							<div class="form-group col-md-3">
								<label class="control-label" for="tipoCombustivel">Combustível</label>
								<g:select name="tipoCombustivel" from="${TipoCombustivel.values()}"  class="form-control"
										  optionValue="nome" value="commandInstance?.tipoCombustivel"/>
							</div>
							<div class="col-md-6">
								<bs:formField id="estabelecimento" name="estabelecimento" value="${commandInstance?.estabelecimento}" label="Estabelecimento" maxlength="15"/>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<bs:formField id="valor" name="valor" value="${commandInstance?.valor}" label="Valor Transação" class="money"/>
							</div>
							<div class="form-group col-md-3">
								<label class="control-label" for="senha">Senha</label>
								<g:passwordField name="senha" class="form-control"/>
							</div>
						</div>


						<div class="buttons">
							<g:if test="${commandInstance?.autorizada}">
								<g:actionSubmit class="btn btn-default" action="confirmFuelTransaction" value="Confirmar" />
								<g:actionSubmit class="btn btn-default" action="confirmFuelTransaction" value="Desfazer" />
							</g:if>
							<g:else>
								<g:actionSubmit class="btn btn-default" action="doFuelTransaction" value="Solicitar" />
							</g:else>
						</div>
					</g:form>
				</div>
			</div>
		</div>
	</body>
</html>
