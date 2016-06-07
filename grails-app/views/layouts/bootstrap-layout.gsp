<%--
  Created by IntelliJ IDEA.
  User: andrecunhas
  Date: 01/06/16
  Time: 11:02
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:layoutTitle default="Grails" /></title>
%{--
    <link rel="shortcut icon" href="${resource(dir:'images',file:'carro.png')}" type="image/x-icon" />
--}%
%{--
    <g:javascript library="jquery" plugin="jquery"/>
--}%

%{--
    <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>
--}%
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/jquery-1.11.3.min.js') }"></script>

    <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap/bootstrap.min.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'estilo-publico.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'font-awesome.min.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'metisMenu.min.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'sb-admin-2.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap-datepicker.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap-sortable.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'estilo.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'prettify.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'sweetalert.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'timeline.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'fonts/fonts.css')}" />
    <link rel="stylesheet" href="${resource(dir:'js',file:'jquery/jquery-ui-1.11.4/jquery-ui.min.css')}"/>

    <!-- DataTables -->
    <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap/dataTables.bootstrap.min.css')}"/>
    <!-- Frota -->
%{--
    <link rel="stylesheet" href="${resource(dir:'css',file:'frota.css')}" />
--}%


%{--
    <script type="text/javascript" src="${resource(dir:'js',file:'application.js') }"></script>
--}%

    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/bootstrap.min.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/sb-admin-2.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/prettify.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/tweenlite.min.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/raphael-min.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/moment.min.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/metisMenu.min.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/bootstrap-waitingfor.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery/jquery.mask.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery/jquery.maskMoney.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery/jquery-ui-1.11.4/jquery-ui.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery/mascaras.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>

    <!-- DataTables -->
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/dataTable/jquery.dataTables.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'plugins/dataTable/dataTables.bootstrap.min.js')}"></script>


    <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>

    %{--
        <script type="text/javascript" src="${resource(dir:'js/jquery/jquery.inputmask',file:'jquery.inputmask.js') }" ></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'maskFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>--}%

    <g:layoutHead />
</head>

<body>
<div id="wrapper">
    <!-- Navigation -->
    <nav id="nav-top" class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <g:if env="homologation">
                <div class="navbar-text flash-enviroment" style="color: green; font-weight: bold">Homologação</div>
            </g:if>
            <g:elseif env="development">
                <div class="navbar-text flash-enviroment"  style="color: #ffff00; font-weight: bold">Desenvolvimento</div>
            </g:elseif>
           %{-- <sec:ifAnyGranted roles="ROLE_EMPRESA">
                <div class="navbar-text">
                    <g:select class="form-control" id="programa" name="programa.id" from="${session.programas}"
                              optionKey="id" required="" value="${session.programa?.id}"
                              noSelection="['': 'Selecione um programa']"
                              onchange="${remoteFunction(controller: "home", action: 'trocarPrograma',
                                      params: '\'programa.id=\' + this.value',
                                      onFailure: 'location.reload')}"/>
                </div>
            </sec:ifAnyGranted>--}%
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown">
                <a data-toggle="dropdown" href="#" class="usuario-logado">
                    <sec:ifLoggedIn>
                        <sec:username/>
                    </sec:ifLoggedIn>
                    <i class="fa fa-user fa-fw"></i>
                </a>
            <li style="
            color: white;
            ">
                |
            </li>
            <sec:ifLoggedIn>
                <li>
                    <g:link controller="logout" class="usuario-logado"><i
                            class="fa fa-sign-out fa-fw"></i> Sair</g:link>
                </li>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <li>
                    <g:link methods="POST" controller="login" class="usuario-logado"><i
                            class="fa fa-sign-in fa-fw"></i> Login</g:link>
                </li>
            </sec:ifNotLoggedIn>
        <!-- /.dropdown-user -->
        </li>
            <!-- /.dropdown -->
        </ul>
        <!-- /.navbar-top-links -->
    </nav>
    <!-- /.navbar-static-side -->

    <div id="#side-menu" class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
                <li id="coop_logo">
                    <a href="${createLink(uri: '/')}"><img alt="VR" src="${resource(dir:'images',file:'vr-beneficios-big.png') }"></a>
                </li>
                <g:render template="/layouts/menu-convenio"/>
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
    </div>

    <div id="page-wrapper">
        <g:layoutBody/>
    </div>

    <sec:ifSwitched>
        <div style="position: fixed; bottom: 1em; right: 1em">
            <a style="border-radius: 0" class="btn btn-danger btn-lg"
               href='${request.contextPath}/j_spring_security_exit_user'>
                <i class="fa fa-arrow-left fa-fw"></i>
                Voltar para <sec:switchedUserOriginalUsername/>
            </a>
        </div>
    </sec:ifSwitched>

    <div class="footer-logado">
        <p style="text-align: center; position: relative !important;"><b>Rede Sysdata</b>® Copyright. Todos os direitos reservados.</p>
    </div>

</div>
</body>
</html>