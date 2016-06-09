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
<br><br>
<table class="table table-bordered" style="font-size: 12px">
	<thead>
	<tr>
		<th>Cartão</th>
		<th>Saldo Disponível</th>
%{--		<th>Senha Atual</th>  --}%
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>${funcionarioInstance?.cartaoAtual()?.numero} [${funcionarioInstance?.cartaoAtual()?.status?.nome}]</td>
		<td>${Util.formatCurrency(funcionarioInstance?.conta?.saldo)}</td>
%{--		<td id="password">  ${funcionarioInstance?.cartaoAtual()?.senha}</td> --}%
	</tr>
	</tbody>
</table>

%{--

<sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADMIN">
	<button class="btn btn-default" onclick="generateNewPassword();">
		<i class="glyphicon glyphicon-lock"></i>
		Gerar Nova Senha
	</button>
</sec:ifAnyGranted>

--}%