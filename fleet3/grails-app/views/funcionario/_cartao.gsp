<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Util" %>

%{--<script type="text/javascript">--}%
	%{--var generateNewPassword=function(){--}%

		%{--if(confirm('A senha atual será ALTERADA. Você confirma a geração da NOVA SENHA?')){--}%
			%{--$.ajax({--}%
				%{--url:"${createLink(action:'generateNewPassword')}",--}%
				%{--data:"id=${funcionarioInstance?.cartaoAtual()?.id}",--}%
				%{--success:function(data){--}%
					%{--$("#password").html(data.newPsw);--}%
				%{--}--}%
			%{--})--}%
		%{--}		--}%
	%{--}--}%
%{--</script>--}%
<br><br>
<table class="table table-bordered" style="font-size: 12px">
	<thead>
	<tr>
		<th>Cartão</th>
		<th>Saldo Total</th>

		<g:if test="${portador.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO}">
			<th>Saldo Mensal</th>
			<th>Saldo Diário</th>
		</g:if>

	</tr>
	</thead>
	<tbody>
	<tr>
		<td>${portador?.cartaoAtual}</td>
		<td>${Util.formatCurrency(portador?.saldoTotal)}</td>
		<g:if test="${portador.unidade.rh.modeloCobranca == TipoCobranca.POS_PAGO}">
			<td>${Util.formatCurrency(portador?.saldoMensal)}</td>
			<td>${Util.formatCurrency(portador?.saldoDiario)}</td>
		</g:if>
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