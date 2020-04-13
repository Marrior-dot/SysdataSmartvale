<html>
    <head>
        <title>Bem vindo ao Sistema Gestão de Frota</title>
        <meta name="layout" content="layout-restrito" />


		<asset:javascript src="application.js"/>


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
			<div class="row">
				<div class="col-md-8">
					<h4><i class="fa fa-bar-chart-o fa-fw"></i> Transações de Combustível (R$)</h4>
				</div>
				<div class="col-md-4">
					<g:form>
						<g:set var="today" value="${new Date()}"/>
						<g:set var="anoAtual" value="${today[Calendar.YEAR]}"/>
						<g:set var="months" value="${['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro', 'Todos']}"/>
						<div class="input-group">
							<g:select name="mesEscolhido" class="form-control" from="${months as List}"
									  value="${months[today[Calendar.MONTH]]}"/>
							<span class="input-group-addon">de</span>
							<g:select name="anoEscolhido" class="form-control" from="${2010..anoAtual}"
									  value="${anoAtual}"/>
						</div>
					</g:form>
				</div>
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
