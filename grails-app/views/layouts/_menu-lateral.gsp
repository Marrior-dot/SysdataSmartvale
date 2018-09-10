<div id="#side-menu" class="navbar-default sidebar" role="navigation">
    <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
            <li class="text-center">
                <a href="${createLink(uri: '/')}"><img alt="logo" src="${resource(dir:'images/projetos/' + projeto?.pasta, file:'logo-small.png') }"></a>
            </li>

            <li>
                <a href="${createLink(uri: '/')}" class="menu-logado"><i class="fa fa-dashboard fa-fw"></i> Início</a>
            </li>

            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH, ROLE_ESTAB">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-archive"></i> Cadastros<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">
                            <li class="controller"><g:link class="submenu-logado" controller="rh" action="list">Empresas</g:link></li>
                            %{--<li class="controller"><g:link class="submenu-logado" controller="funcionario" action="list">Funcionários</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="veiculo" action="list">Veículos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="equipamento" action="list">Equipamentos</g:link></li>--}%
                        </sec:ifAnyGranted>

                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado"  controller="postoCombustivel" action="list">Credenciados</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="produto" action="list">Produtos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado"  controller="arquivo" action="list">Arquivos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado"  controller="marcaVeiculo" action="list">Marcas de Veículos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado"  controller="tipoEquipamento" action="list">Tipos de Equipamentos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="motivoNegacao" action="list">Motivos Negação</g:link></li>
                        </sec:ifAnyGranted>

                        <sec:ifAnyGranted roles="ROLE_ESTAB">
                            <li class="controller"><g:link class="submenu-logado" controller="postoCombustivel" action="list">Estabelecimentos</g:link></li>
                        </sec:ifAnyGranted>

                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="auditLogEvent" action="list">Audit Log</g:link></li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-cogs"></i> Financeiro<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="pedidoCarga" action="list">Pedido de Carga</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="transacao" action="list">Transações</g:link></li>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="transacao" action="listPendentes">Transações Pendentes</g:link></li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-phone-square"></i> Central de Atendimento<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'unlockNewCard']">Desbloqueio de Cartão</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'cancelCard']">Cancelamento de Cartão</g:link></li>
                        %{--<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="settingPriceTransaction">Configuração de Preços de Combustível</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="fuelTransaction" >Transação de Combustível</g:link></li>
                        </sec:ifAnyGranted>--}%
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-file-text"></i>  Relatório <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link  class="submenu-logado"  controller="reportViewer">Relatórios</g:link></li>
                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="report">Configuração Relatório</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="fieldReport">Campos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="parameterReport">Parâmetros</g:link></li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-shield fa-fw"></i> Segurança<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link controller="user" class="submenu-logado" action="index">Usuários</g:link>
                        </li>
                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <li>
                                <g:link controller="role" class="submenu-logado" action="index">Perfis de Acesso</g:link>
                            </li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_PROC">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-wrench fa-fw"></i> Sistema<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link class="submenu-logado" controller="console" target="Console">Console</g:link>
                            <g:link class="submenu-logado" controller="processamento" >Processamentos</g:link>
                        </li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_LOG, ROLE_HELP">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-archive"></i> Cadastros<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado"  controller="postoCombustivel" action="list">Empresas Lojistas</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="funcionario" action="show" id="${sec.loggedInUserInfo(field: 'id')}">Funcionarios</g:link></li>
                    </ul>
                </li>
            </sec:ifAnyGranted>


            <sec:ifAnyGranted roles="ROLE_ESTAB">
                <li class="controller"><g:link class="submenu-logado" controller="user" action="meusDados" id="${sec.loggedInUserInfo(field: 'id')}">Meus Dados</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="transacao" action="index">Transações</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="reportViewer">Relatórios</g:link></li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_RH">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-cogs"></i> Financeiro<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="pedidoCarga" action="index">Solicitação de Carga</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="transacao" action="list">Transações</g:link></li>
                    </ul>
                </li>

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-wrench fa-fw"></i> Sistema<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="user" action="meusDados" id="${sec.loggedInUserInfo(field: 'id')}">Meus Dados</g:link></li>
                    </ul>
                </li>
            </sec:ifAnyGranted>
        </ul>
    </div>
    <!-- /.sidebar-collapse -->
</div>