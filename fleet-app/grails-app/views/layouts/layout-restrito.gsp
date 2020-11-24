<%--
  Created by IntelliJ IDEA.
  User: andrecunhas
  Date: 01/06/16
  Time: 11:02
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:set var="projeto" value="${grailsApplication.config.projeto}"/>
    <g:render template="/layouts/carregar-arquivos-customizados" model="[projeto: projeto]"/>

    <asset:stylesheet src="font-awesome.min.css" />
    <asset:stylesheet src="metisMenu.min.css" />
    <asset:stylesheet src="sb-admin-2.css" />
    <asset:stylesheet src="bootstrap-datepicker.css" />
    <asset:stylesheet src="bootstrap-sortable.css" />
    <asset:stylesheet src="prettify.css" />
    <asset:stylesheet src="sweetalert.css" />
    <asset:stylesheet src="timeline.css" />
    <asset:stylesheet src="fonts/fonts.css" />


    <asset:javascript src="application.js"/>

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
                <div class="navbar-text flash-enviroment" style="color: white; font-weight: bold">Homologação</div>
            </g:if>
            <g:elseif env="development">
                <div class="navbar-text flash-enviroment"  style="color: white; font-weight: bold">Desenvolvimento</div>
            </g:elseif>
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
            <li style="color: white;">|</li>
                <sec:ifLoggedIn>
                    <li><g:link controller="logout" class="usuario-logado"><i class="fa fa-sign-out fa-fw"></i> Sair</g:link></li>
                </sec:ifLoggedIn>
                <sec:ifNotLoggedIn>
                    <li><g:link methods="POST" controller="login" class="usuario-logado"><i class="fa fa-sign-in fa-fw"></i> Login</g:link></li>
                </sec:ifNotLoggedIn>
            </li>
        </ul>
    </nav>

    <g:render template="/layouts/menu-lateral" model="[projeto: projeto]"/>


    <div id="page-wrapper">
        <g:layoutBody/>
    </div>

    <sec:ifSwitched>
        <div style="position: fixed; bottom: 5em; right: 1em">
            <a style="border-radius: 0" class="btn btn-danger btn-lg"
               href='${request.contextPath}/j_spring_security_exit_user'>
                <i class="fa fa-arrow-left fa-fw"></i>
                Voltar para <sec:switchedUserOriginalUsername/>
            </a>
        </div>
    </sec:ifSwitched>
</div>

</body>
</html>