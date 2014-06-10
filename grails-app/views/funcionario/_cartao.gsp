<%@ page import="com.sysdata.gestaofrota.Util" %>

<script type="text/javascript">

	var generateNewPassword=function(){

		if(confirm('A senha atual será ALTERADA. Você confirma a geração da NOVA SENHA?')){
			$.ajax({
				url:"${createLink(action:'generateNewPassword')}",
				data:"id=${funcionarioInstance?.cartaoAtual()?.id}",
				success:function(data){
					$("#password").html(data.newPsw);
				}
			})
		}

		
	}

</script>

<style>
	#password{
		font-weight:normal;
	}
</style>


<fieldset class="uppercase">
	<div>
		<label><span>Cartão</span>${funcionarioInstance?.cartaoAtual()?.numero} [${funcionarioInstance?.cartaoAtual()?.status?.nome}]</label>
		<div class="clear"></div>
	</div>
	<div>
		<label><span>Saldo Disponível</span>${Util.formatCurrency(funcionarioInstance?.conta?.saldo)}</label>
		<div class="clear"></div>
	</div>
	<div>
		<label><span>Senha Atual</span><span id="password">${funcionarioInstance?.cartaoAtual()?.senha}</span></label>
		<div class="clear"></div>
	</div>

	<sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADMIN">
		<button onclick="generateNewPassword();">Gerar Nova Senha</button>	
	</sec:ifAnyGranted>

	
</fieldset>

