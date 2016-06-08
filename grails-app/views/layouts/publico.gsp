<%--
  Created by IntelliJ IDEA.
  User: andrecunhas
  Date: 01/06/16
  Time: 11:22
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'carro.png')}" type="image/x-icon" />

        <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap/bootstrap.min.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'estilo-publico.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
        <g:layoutHead />
    </head>

    <body>
        <div id="wrapper">
            <div class="hidden-xs">
                <div id="nav-top" class="navbar navbar-default navbar-static-top" role="navigation"
                     style="margin-bottom: 0;    height: 80px;">

                    <div class="navbar-left" style="margin-left:10px; margin-top:10px">
                        <img alt="VR" src="${resource(dir:'images',file:'vr-beneficios-small.png') }">
                    </div>

                </div>
            </div>

            <div id="page-body">
                <g:layoutBody/>
            </div>

            <div class="hidden-xs">
                <g:render template="/layouts/public-footer"/>
            </div>

            <div  class="visible-xs">
                <div>
                    <p style="text-align: center;"><b>Rede Sysdata</b>® Copyright. Todos os direitos reservados.</p>
                </div>

            </div>
        </div>
    </body>
</html>