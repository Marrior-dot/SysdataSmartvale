<%@ page import="com.sysdata.gestaofrota.Rh" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="Cliente"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="body">

    <div class="panel panel-default panel-top">

        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]"/></h4>
        </div>

        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>
            <g:form>

                <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                    <div class="buttons">
                        <a class="btn btn-default" href="${createLink(uri: '/')}"><span
                                class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                        <g:link action="create" class="btn btn-default">
                            <span class="glyphicon glyphicon-plus"></span>
                            <g:message code="default.new.label" args="[entityName]"/>
                        </g:link>
                    </div>
                </sec:ifAnyGranted>

                <div class="panel-top">
                    <g:render template="search"></g:render>
                </div>

            </g:form>
        </div>

    </div>
</div>
</body>
</html>