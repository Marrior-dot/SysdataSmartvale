<html>
    <head>
        <title>Menu Gestão de Frota</title>
        <meta name="layout" content="main" />
    </head>
    <body>
    	<div class="nav">
    		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    	</div>

           	<div id="user">
        		<img src="${resource(dir:'images',file:'usuario.png')}" alt="Usuário Cenpros" border="0"/>
        		<span style="font: 20px sans-serif ;color: #4682B4 " ><sec:username />, bem vindo(a) ao Gestão de Frota</span>
        	</div>

			<hr>

   			<g:if test="${servico=='cadastros'}">
                <h2>Cadastros</h2>
                <ul>
                	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_RH">
						<li><g:link controller="rh" action="list">Programas</g:link></li></br>
						<li><g:link controller="funcionario" action="list">Funcionários</g:link></li></br>
						<li><g:link controller="veiculo" action="list">Veículos</g:link></li></br>
						<li><g:link controller="equipamento" action="list">Equipamentos</g:link></li></br>
					</sec:ifAnyGranted>

					<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
						<li><g:link controller="postoCombustivel" action="list">Estabelecimentos</g:link></li></br>
						<li><g:link controller="produto" action="list">Produtos</g:link></li></br>
						<li><g:link controller="arquivo" action="list">Arquivos</g:link></li></br>
						<li><g:link controller="user" action="list">Usuários</g:link></li></br>

						<li><g:link controller="estado" action="list">Estados</g:link></li></br>
						<li><g:link controller="cidade" action="list">Cidades</g:link></li></br>
						<li><g:link controller="marcaVeiculo" action="list">Marcas de Veículos</g:link></li></br>
						<li><g:link controller="tipoEquipamento" action="list">Tipos de Equipamentos</g:link></li></br>
						<li><g:link controller="banco" action="list">Bancos</g:link></li></br>

						<li><g:link controller="role" action="list">Roles</g:link></li></br>
					</sec:ifAnyGranted>

					<sec:ifAnyGranted roles="ROLE_ESTAB,ROLE_LOG">
						<li><g:link controller="postoCombustivel" action="list">Estabelecimentos</g:link></li></br>
					</sec:ifAnyGranted>

					<sec:ifAnyGranted roles="ROLE_PROC">
						<li><g:link controller="parametroSistema" action="list">Parâmetros do Sistema</g:link></li></br>
						<li><g:link controller="motivoNegacao" action="list">Motivos Negação</g:link></li></br>
						<li><g:link controller="auditLogEvent" action="list">Audit Log</g:link></li></br>
					</sec:ifAnyGranted>

                </ul>
   			</g:if>

			<g:if test="${servico=='financeiro'}">
				<h2>Financeiro</h2>
				<ul>
					<li><g:link controller="pedidoCarga" action="list">Pedido de Carga Michel</g:link></li></br>
					<li><g:link controller="transacao" action="list">Transações</g:link></li></br>
				<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
					<li><g:link controller="transacao" action="listPendentes">Transações Pendentes</g:link></li></br>
				</sec:ifAnyGranted>
				</ul>
			</g:if>

			<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_ESTAB, ROLE_RH,ROLE_LOG,ROLE_HELP">
				<g:if test="${servico=='relatorios'}">
					<h2>Relatórios</h2>
					<ul>
						<sec:ifAnyGranted roles="ROLE_PROC">
							<li><g:link controller="report" action="list">Config. Relatório</g:link></li></br>
							<li><g:link controller="parameterReport" action="list">Config. Parâmetros</g:link></li></br>
							<li><g:link controller="fieldReport" action="list">Config. Campos </g:link></li></br>
						</sec:ifAnyGranted>
						<li><g:link controller="reportViewer" action="listReports">Relatórios</g:link></li></br>
					</ul>
				</g:if>

			</sec:ifAnyGranted>
			<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_RH">
				<g:if test="${servico=='ca'}">
					<h2>Central de Atendimento</h2>
					<ul>
						<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
							<li><g:link controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'unlockNewCard']">Desbloqueio de Novo Cartão</g:link></li></br>
							<li><g:link controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'cancelCard']">Cancelamento de Cartão</g:link></li></br>
						</sec:ifAnyGranted>

						<li><g:link controller="centralAtendimento" action="searchCards" params="[act:'buscarFuncionarios',goTo:'transfSaldo']">Transferência de Saldo</g:link></li></br>


						<sec:ifAnyGranted roles="ROLE_PROC">
							<li><g:link controller="centralAtendimento" action="settingPriceTransaction" >Configuração de Preços de Combustível</g:link></li></br>
							<li><g:link controller="centralAtendimento" action="fuelTransaction" >Transação de Combustível</g:link></li></br>
						</sec:ifAnyGranted>
					</ul>
				</g:if>
			</sec:ifAnyGranted>


    </body>
</html>
