<%@ page contentType="text/html;charset=UTF-8" %>
<sec:ifAnyGranted roles="ROLE_PROC">
    <li>
        <a href="${createLink(uri: '/')}" class="menu-logado"><i class="fa fa-dashboard fa-fw"></i> Início</a>
    </li>
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-archive"></i> Cadastros<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li class="controller"><g:link class="submenu-logado" controller="administradora">Administradora</g:link></li>
            %{--<li class="controller"><g:link class="submenu-logado" controller="categoriaCartao">Categoria de Cartões</g:link></li>--}%
            <sec:ifAnyGranted roles="ROLE_MASTER, ROLE_SUPORTE, ROLE_ADMINISTRADORA">
                <li class="controller"><g:link class="submenu-logado" controller="empresa">Empresas</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="unidade" action="index">Unidades</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="contaPortador" action="index">Portadores</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="grupoEstabelecimento">Grupo Estabelecimentos</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="estabelecimento">Estabelecimentos</g:link></li>
               %{-- <sec:ifAnyGranted roles="ROLE_MASTER, ROLE_SUPORTE">
                    <li class="controller"><g:link class="submenu-logado" controller="programa">Programas</g:link></li>
                </sec:ifAnyGranted>--}%
            </sec:ifAnyGranted>
        </ul>
        <!-- /.nav-second-level -->
    </li>
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-cogs"></i> Operação<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
  %{--          <li class="controller"><g:link class="submenu-logado" controller="arquivo" action="listEmbossing">Arquivo de Embossing</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="arquivo" action="listCartaSenha">Arquivo Carta Senha</g:link></li>--}%
            <li class="controller"><g:link class="submenu-logado" controller="arquivo" action="listCartaSenha">Arquivos Carta Senha</g:link>
            <li class="controller"><g:link class="submenu-logado" controller="arquivo" action="listEmbossing">Arquivos de Embossing</g:link>
            <li class="controller"><g:link class="submenu-logado" controller="cartao">Cartões</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="consulta" action="saldoExtrato">Consultar Saldo e Extrato</g:link></li>
            %{--<li class="controller"><g:link class="submenu-logado" controller="solicitacaoCartao" action="transferenciaCarga">Transferência de Saldo</g:link></li>--}%
            <li class="controller"><g:link class="submenu-logado" controller="solicitacaoCarga" action="index">Solicitação de Carga</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="solicitacaoCarga" action="estorno">Estorno de Carga</g:link></li>
            %{--<li class="controller"><g:link class="submenu-logado" controller="solicitacaoCartao">Solicitação de Cartão</g:link></li>--}%
            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="desbloqueio">Desbloquear Cartão</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="desbloqueioLote">Desbloquear Cartão em Lote</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="cancelamento">Cancelar Cartão</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="cancelamentoLote">Cancelar Cartão em Lote</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="cartao" action="zerarSaldo">Zerar Saldo Cartão</g:link></li>
            <li class="controller"><g:link class="submenu-logado" controller="transacao">Transações</g:link></li>

        </ul>
        <!-- /.nav-second-level -->
    </li>
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-file-text"></i>  Relatório <span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li class="controller"><g:link  class="submenu-logado"  controller="reportViewer">Relatórios</g:link></li>
            <sec:ifAnyGranted roles="ROLE_MASTER, ROLE_SUPORTE">
                <li class="controller"><g:link class="submenu-logado" controller="report">Configuração Relatório</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="fieldReport">Campos</g:link></li>
                <li class="controller"><g:link class="submenu-logado" controller="parameterReport">Parâmetros</g:link></li>
            </sec:ifAnyGranted>
        </ul>
        <!-- /.nav-second-level -->
    </li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_MASTER, ROLE_SUPORTE">
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-wrench fa-fw"></i> Sistema<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li>
                <g:link class="submenu-logado" controller="console" target="Console">Console</g:link>
            </li>
            <li>
                <g:link class="submenu-logado" controller="configuracaoAtributo">Configuração Atributo</g:link>
            </li>

            <li>
                <g:link class="submenu-logado" controller="configuracaoParametro">Configuração Parâmetro</g:link>
            </li>
            <li>
                <g:link class="submenu-logado" controller="status">Status</g:link>
            </li>
            <li>
                <g:link class="submenu-logado" controller="mailTemplate">Templates Email</g:link>
            </li>

        </ul>
        <!-- /.nav-second-level -->
    </li>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_MASTER, ROLE_ADMINISTRADORA, ROLE_SUPORTE">
    <li>
        <a href="#" class="menu-logado"><i class="fa fa-shield fa-fw"></i> Segurança<span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li>
                <g:link controller="user" class="submenu-logado" action="meuUsuario">Meu Usuário</g:link>
            </li>
            <li>
                <g:link controller="user" class="submenu-logado" action="index">Usuários</g:link>
            </li>
            <sec:ifAnyGranted roles="ROLE_MASTER, ROLE_SUPORTE">
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
