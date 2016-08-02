7<html>
    <head>
        <title>Bem vindo ao Sistema Gestão de Frota</title>
        <meta name="layout" content="bootstrap-layout" />
		<script type="text/javascript" src="${resource(dir:'js',file:'chart-config.js') }"></script>
		<script type="text/javascript" src="${resource(dir:'js',file:'plugins/Chart.min.js') }"></script>
		<style>
		#chartjs-tooltip {
			opacity: 0;
			position: absolute;
			background: rgba(0, 0, 0, .7);
			color: white;
			padding: 3px;
			border-radius: 3px;
			-webkit-transition: all .1s ease;
			transition: all .1s ease;
			pointer-events: none;
			-webkit-transform: translate(-50%, 0);
			transform: translate(-50%, 0);
		}

		</style>


	</head>
    <body>
	<br><br>
	<div class="panel panel-default">
		<div class="panel-heading">
			<i class="fa fa-bar-chart-o fa-fw"></i> Transações de Combustível (R$)
			<div class="pull-right">
				<g:form>
					%{--<g:select name="mesEscolhido" class="form-down-option-graphics"
							  from="${['Todos', 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez']}" ></g:select>
					<g:select name="anoEscolhido" class="form-down-option-graphics" from="${2016..2012}"></g:select>--}%
					<g:set var="today" value="${new Date()}"/> %{--Seta a data de Hoje--}%
					<g:set var="months" value="${['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez','Todos']}"/> %{--Faz uma lista dos meses--}%
					<g:select name="mesEscolhido" class="form-down-option-graphics"
							  from="${months as List}" value="${months[today[Calendar.MONTH]]}"></g:select> %{--Faz o select dos meses no value mostrando o mes atual--}%
					<g:select name="anoEscolhido" class="form-down-option-graphics" from="${2014..2020}" value="${today[Calendar.YEAR]}"></g:select> %{--Faz o select dos anos no value mostrando o ano atual--}%

				</g:form>
			</div>
		</div>
		<div class="panel-body">
			<div id="line-chart-transacoes">

			</div>
		</div>
		<ul class="chart-legend">
			<li>
				<span style="background-color:#58CE58"></span>
				Confirmadas
			</li>
			<sec:ifAnyGranted roles="ROLE_PROC, ROLE_ADMIN">
				<li>
					<span style="background-color:#EA4335"></span>
					Não Autorizadas
				</li>
				<li>
					<span style="background-color:#4386F5"></span>
					Desfeitas
				</li>
				<li>
					<span style="background-color:#FBBC05"></span>
					Pendentes
				</li>
				<li>
					<span style="background-color:#434343"></span>
					Canceladas
				</li>
			</sec:ifAnyGranted>
		</ul>
	</div>
    </body>
</html>
