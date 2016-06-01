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
    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'carro.png')}" type="image/x-icon" />
    <g:javascript library="jquery" plugin="jquery"/>
    <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>
    <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap/bootstrap.min.css')}" />

    <script type="text/javascript" src="${resource(dir:'js/jquery/jquery.inputmask',file:'jquery.inputmask.js') }" ></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'maskFields.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>

    <g:layoutHead />
</head>

<body>
    <g:layoutBody />
</body>
</html>