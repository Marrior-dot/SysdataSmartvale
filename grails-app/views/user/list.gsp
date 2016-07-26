
<%@ page import="com.sysdata.gestaofrota.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'table-javascript-style.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'frota.css')}" />

        <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>

        <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>

    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="buttons">
                <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                <sec:ifAnyGranted roles="ROLE_PROC,ROLE_ADMIN">

                    <g:link action="create" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus"></span>
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link>

                </sec:ifAnyGranted>
            </div>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>

               <br>

                <g:render template="search" model="[controller:'user']"/>
            </div>
        </div>
    </div>

    </body>
</html>
