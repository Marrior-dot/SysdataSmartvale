<%@ page contentType="text/html;charset=UTF-8" %>
<sec:ifAnyGranted roles="ROLE_PROC">
    <li>
        <a href="${createLink(uri: '/')}" class="menu-logado"><i class="fa fa-dashboard fa-fw"></i> Início</a>
    </li>
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-archive"></i> Cadastros<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_RH">
                <li class="controller"><g:link class="submenu-logado" controller="rh" action="list">Programas</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="funcionario" action="list">Funcionários</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="veiculo" action="newList">Veículos</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="equipamento" action="newList">Equipamentos</g:link></li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <li class="controller"><g:link class="submenu-logado"  controller="postoCombustivel" action="list">Empresas Lojistas</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="produto" action="list">Produtos</g:link></li>
                <li class="controller"><g:link class="submenu-logado"  controller="arquivo" action="newList">Arquivos</g:link></li>
%{--
                <li class="controller"><g:link class="submenu-logado" controller="user" action="list">Usuários</g:link></li>
--}%

%{--
                <li class="controller"><g:link class="submenu-logado"  controller="estado" action="list">Estados</g:link></li>
--}%
%{--
                <li class="controller"><g:link class="submenu-logado" controller="cidade" action="list">Cidades</g:link></li>
--}%
                <li class="controller"><g:link class="submenu-logado"  controller="marcaVeiculo" action="list">Marcas de Veículos</g:link></li>
                <li class="controller"><g:link class="submenu-logado"  controller="tipoEquipamento" action="list">Tipos de Equipamentos</g:link></li>
%{--
                <li class="controller"><g:link class="submenu-logado"  controller="banco" action="list">Bancos</g:link></li>
--}%

%{--
                <li class="controller"><g:link class="submenu-logado" controller="role" action="list">Roles</g:link></li>
--}%
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ESTAB,ROLE_LOG">
                <li class="controller"><g:link class="submenu-logado" controller="postoCombustivel" action="list">Estabelecimentos</g:link></li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_PROC">
%{--
                <li class="controller"><g:link class="submenu-logado" controller="parametroSistema" action="list">Parâmetros do Sistema</g:link></li>
--}%
                <li class="controller"><g:link class="submenu-logado" controller="motivoNegacao" action="list">Motivos Negação</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="auditLogEvent" action="list">Audit Log</g:link></li>
            </sec:ifAnyGranted>
        </ul>
        <!-- /.nav-second-level -->
    </li>
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
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_PROC">
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-wrench fa-fw"></i> Sistema<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li>
                <g:link class="submenu-logado" controller="console" target="Console">Console</g:link>
            </li>
        </ul>
        <!-- /.nav-second-level -->
    </li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_PROC">
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

<sec:ifAnyGranted roles="ROLE_CA">
    <li class="controller"><g:link class="menu-logado" controller="consulta" action="saldoExtrato">Consultar Saldo e Extrato</g:link></li>
    <li class="controller"><g:link class="menu-logado" controller="solicitacaoCartao" action="transferenciaCarga">Transferência de Saldo</g:link></li>
    <li class="controller"><g:link class="submenu-logado" controller="cartao" action="desbloqueio">Desbloquear Cartão</g:link></li>
    <li class="controller"><g:link class="submenu-logado" controller="cartao" action="cancelamento">Cancelar Cartão</g:link></li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_LOJISTA">
    <li class="controller"><g:link class="menu-logado" controller="transacao" action="index">Transações</g:link></li>
    <li class="controller"><g:link  class="submenu-logado"  controller="reportViewer">Relatórios</g:link></li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_UNIDADE">
    <li class="controller"><g:link class="submenu-logado" controller="contaPortador" action="index">Portadores</g:link></li>
    <li class="controller"><g:link class="submenu-logado" controller="solicitacaoCarga" action="create">Solicitação de Carga</g:link></li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_EMPRESA">
    <li class="controller"><g:link class="submenu-logado" controller="contaPortador" action="index">Portadores</g:link></li>
    <li class="controller"><g:link class="submenu-logado" controller="solicitacaoCarga" action="create">Solicitação de Carga</g:link></li>
    <li class="controller"><g:link class="submenu-logado" controller="unidade" action="index">Unidades</g:link></li>
</sec:ifAnyGranted>
