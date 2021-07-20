<%@ page import="com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.Rh" %>
<div id="#side-menu" class="navbar-default sidebar" role="navigation">
    <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
            <li class="text-center">
                <a href="${createLink(uri: '/')}"><img alt="logo" width=220 src="${assetPath(src:'projetos/' + projeto?.pasta + '/logo-small.png') }"></a>
            </li>

            <li>
                <a href="${createLink(uri: '/')}" class="menu-logado"><i class="fa fa-dashboard fa-fw"></i> Início</a>
            </li>

            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-archive"></i> Cadastros<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">

                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="rh" action="list">Clientes</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="postoCombustivel" action="list">Credenciados</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="produto" action="list">Produtos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="arquivo" action="list">Arquivos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="marcaVeiculo" action="list">Marcas de Veículos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="tipoEquipamento" action="list">Tipos de Equipamentos</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="list">Cartões</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="loteEmbossing">Embossing Cartões</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="motivoNegacao" action="list">Motivos Negação</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="cidade" action="list">Cidades</g:link></li>
                        </sec:ifAnyGranted>

                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <li class="controller"><g:link class="submenu-logado" controller="auditLogEvent" action="list">Audit Log</g:link></li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_PROC_FINANC">

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-money"></i> Financeiro<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="lotePagamento">Lotes Repasses Conveniados</g:link></li>
                    </ul>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="loteRecebimento">Lotes Recebimentos Clientes</g:link></li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

            </sec:ifAnyGranted>


            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-cogs"></i> Operação<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="solicitacaoCartaoProvisorio">Solicitações Cartões Provisórios</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="pedidoCarga" action="list">Pedidos de Carga</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="transacao" action="list">Transações</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="transacao" action="listAdmin">Transações Administrativas</g:link></li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

                <g:if test="${grailsApplication.config.projeto.features == ["lotePagamento", "loteRecebimento"]}">
                    <li>
                        <a href="#" class="menu-logado"><i class="fa fa-money"></i> Financeiro<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li class="controller"><g:link class="submenu-logado" controller="lotePagamento">Lotes Repasses Conveniados</g:link></li>
                            <li class="controller"><g:link class="submenu-logado" controller="loteRecebimento">Lotes Recebimentos Clientes</g:link></li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                </g:if>

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-shield fa-fw"></i>&nbsp;Segurança<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link controller="user" class="submenu-logado" action="index">Usuários</g:link>
                        </li>
                        <sec:ifAnyGranted roles="ROLE_PROC">
                            <li>
                                <g:link controller="role" class="submenu-logado" action="index">Perfis de Acesso</g:link>
                            </li>
                        </sec:ifAnyGranted>
                        <li class="controller"><g:link class="submenu-logado" controller="user" action="meuUsuario" id="${sec.loggedInUserInfo(field: 'id')}">Meu Usuário</g:link></li>

                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>



            <sec:ifAnyGranted roles="ROLE_PROC">
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-wrench fa-fw"></i>&nbsp;Sistema<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link class="submenu-logado" controller="console" target="Console">Console</g:link>
                            <g:link class="submenu-logado" controller="processamento" >Processamentos</g:link>
                            <g:link class="submenu-logado" controller="mockTransacao" >Gerador Transações</g:link>
                            <g:link class="submenu-logado" controller="configuracaoPropriedade" >Configurador de Propriedades</g:link>
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
                <li class="controller"><g:link class="submenu-logado" controller="user" action="meusDados" id="${sec.loggedInUserInfo(field: 'id')}">&nbsp;Meus Dados</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="transacao" action="index">&nbsp;Transações</g:link></li>

            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_RH">
                <li class="controller"><g:link class="submenu-logado" controller="user" action="meusDados" id="${sec.loggedInUserInfo(field: 'id')}">&nbsp;Meus Dados</g:link></li>
                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-cogs"></i>&nbsp;Operação<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">

                        <sys:propertyOwner ownerType="${Rh}"  property="modeloCobranca" value="${TipoCobranca.PRE_PAGO}">
                            <li class="controller"><g:link class="submenu-logado" controller="pedidoCarga" action="index">Solicitação de Carga</g:link></li>
                        </sys:propertyOwner>


                        <li class="controller"><g:link class="submenu-logado" controller="transacao" action="list">Transações</g:link></li>
                    </ul>
                </li>
            </sec:ifAnyGranted>


            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH, ROLE_ESTAB">

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-file-text"></i>  Relatório <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        %{--<li class="controller"><g:link  class="submenu-logado"  controller="reportViewer">Relatórios</g:link></li>--}%

                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_ESTAB">
                            <li class="controller"><g:link  class="submenu-logado"  controller="projecaoReembolsoRelatorio">Projeção de Reembolso</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="reembolsoFaturadoRelatorio">Reembolsos Faturados</g:link></li>
                        </sec:ifAnyGranted>

                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">
                            <li class="controller"><g:link  class="submenu-logado"  controller="consumoProdutosRelatorio">Consumo de Combustíveis</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="demonstrativoDesempenhoRelatorio">Demonstrativo Desempenho</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="controleMensalCargasRelatorio">Controle Mensal de Cargas</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="baseEstabelecimentosRelatorio">Base de Estabelecimentos</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="baseFuncionariosRelatorio">Base de Funcionários</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="baseEquipamentosRelatorio">Base de Equipamentos</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="baseVeiculosRelatorio">Base de Veículos</g:link></li>
                            <li class="controller"><g:link  class="submenu-logado"  controller="historicoFrotaRelatorio">Histórico de Transações Frota</g:link></li>
                        </sec:ifAnyGranted>

                        %{--
                                                <sec:ifAnyGranted roles="ROLE_PROC">
                                                    <li class="controller"><g:link class="submenu-logado" controller="report">Configuração Relatório</g:link></li>
                                                    <li class="controller"><g:link class="submenu-logado" controller="fieldReport">Campos</g:link></li>
                                                    <li class="controller"><g:link class="submenu-logado" controller="parameterReport">Parâmetros</g:link></li>
                                                </sec:ifAnyGranted>
                        --}%
                    </ul>
                    <!-- /.nav-second-level -->
                </li>


            </sec:ifAnyGranted>


            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">

                <li>
                    <a href="#" class="menu-logado"><i class="fa fa-phone-square"></i> Central de Atendimento<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'unlockNewCard']">Desbloqueio de Cartão</g:link></li>
                        <li class="controller"><g:link class="submenu-logado" controller="centralAtendimento" action="searchCard" params="[act:'findFuncionario',goTo:'cancelCard']">Cancelamento de Cartão</g:link></li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>

            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_RH, ROLE_ESTAB, ROLE_PROC_FINANC">
                <li class="controller"><g:link class="submenu-logado" controller="user" action="meuUsuario" id="${sec.loggedInUserInfo(field: 'id')}">&nbsp;Meu Usuário</g:link></li>

            </sec:ifAnyGranted>


        </ul>
    </div>
    <!-- /.sidebar-collapse -->
</div>