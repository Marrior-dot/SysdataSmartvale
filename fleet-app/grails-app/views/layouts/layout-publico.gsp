<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:set var="projeto" value="${grailsApplication.config.projeto}"/>
    <g:render template="/layouts/carregar-arquivos-customizados" model="[projeto: projeto]"/>


    <style type="text/css">

        body {
            background-size: 100%;
        }

        div#wrapper {

            background: url("${assetPath(src: 'projetos/' + projeto.pasta + '/background-publico.png')}") no-repeat;
            #height: 100vh;
            background-size: cover;
        }

    </style>

    <g:layoutHead/>
</head>

<body>
<div id="wrapper">

    <div class="hidden-xs">
        <div role="navigation"
             style="padding-top: 20px; height: 110px; display: flex; justify-content: center;">
            <div class="navbar-left" style="margin: 0 auto;">

            </div>
        </div>
    </div>

    <div class="container" style="height: 70%; display: flex; align-items: center;">
        <g:layoutBody/>
    </div>

    <div class="footer" >
        <strong>Rede Sysdata</strong>® Copyright. Todos os direitos reservados.
    </div>
</div>
</body>
</html>