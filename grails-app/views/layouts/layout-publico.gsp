<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:set var="projeto" value="${grailsApplication.config.project}"/>
    <g:render template="/layouts/carregar-arquivos-customizados" model="[projeto: projeto]"/>

    <style type="text/css">
    body {
        background: url(${resource(dir:'images/projetos/' + projeto.pasta, file:'background-publico.png')}) no-repeat fixed left;
        background-size: 100%;
    }
    </style>
    <g:layoutHead/>
</head>

<body>
<div id="wrapper">
    <div class="hidden-xs">
        <div id="nav-top" class="navbar navbar-default navbar-static-top" role="navigation"
             style="margin-bottom: 0;height: 110px;">
            <div class="navbar-left" style="margin-left:10px; margin-top:10px">
                <img alt="logo" src="${resource(dir: 'images/projetos/' + projeto?.pasta, file: 'logo.png')}">
            </div>
        </div>
    </div>

    <div class="container">
        <g:layoutBody/>
    </div>

    <div class="footer text-center">
        <strong>Rede Sysdata</strong>® Copyright. Todos os direitos reservados.
    </div>
</div>
</body>
</html>