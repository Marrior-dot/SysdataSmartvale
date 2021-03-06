<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <br/>
        <div class="body">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><g:message code="default.show.label" args="[entityName]" /></h4>
                </div>
                <div class="panel-body">

                    <g:hasErrors bean="${userInstance}">
                        <div class="alert alert-danger" role="alert">
                            <strong>Erro ao salvar ${entityName} </strong>
                            <g:renderErrors bean="${userInstance}" as="list" />
                        </div>
                    </g:hasErrors>

                    <alert:all/>


                    <div class="buttons-top">
                        <sec:ifAnyGranted roles="ROLE_PROC, ROLE_ADMIN">
                            <g:link class="btn btn-default" action="list">
                                <span class="glyphicon glyphicon-list"></span>
                                <g:message code="default.list.label" args="[entityName]" />
                            </g:link>

                            <g:if test="${action == Util.ACTION_VIEW}">
                                <g:link class="btn btn-default" action="create">
                                    <span class="glyphicon glyphicon-plus"></span>
                                    <g:message code="default.new.label" args="[entityName]" />
                                </g:link>
                            </g:if>
                        </sec:ifAnyGranted>

                        <g:if test="${action == Util.ACTION_VIEW}">
                            <g:link action="editPassword" class="btn btn-default">
                                <span class="glyphicon glyphicon-edit"></span>&nbsp;Alterar Senha
                            </g:link>
                        </g:if>

                    </div>


                    <g:render template="basico" model="${[action: action]}"/>
                </div>
            </div>
        </div>
    </body>
</html>
