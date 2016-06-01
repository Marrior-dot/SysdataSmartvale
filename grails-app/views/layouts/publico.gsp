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
    <g:javascript library="jquery" plugin="jquery"/>
    <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>

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


    <script type="text/javascript" src="${resource(dir:'js/jquery/jquery.inputmask',file:'jquery.inputmask.js') }" ></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'maskFields.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>

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

    <!--MOBILE-->

    <div class="visible-xs">
        <div class="navbar navbar-default navbar-static-top" role="navigation"
             style="margin-bottom: 0;    height: 0px;">
            <nav class="navbar navbar-default navbar-venda-facil" role="navigation">

                <div class="selo-solicite-ja">
                    <img alt="VR" src="https://www.vr.com.br/portal/consolidador/images/vr-beneficios-85.png">
                </div>

            </nav>
        </div>
    </div>
    <!--/MOBILE-->

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
</body>
</html>