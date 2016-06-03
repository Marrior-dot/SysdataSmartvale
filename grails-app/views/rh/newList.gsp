
<%@ page import="com.sysdata.gestaofrota.Rh" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'rh.label', default: 'RH')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="body">
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <g:form>

                <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                    <div class="buttons">
                        <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                        <g:link action="create" class="btn btn-default">
                            <span class="glyphicon glyphicon-plus"></span>
                            <g:message code="default.new.label" args="[entityName]" />
                        </g:link>
                    </div>
                </sec:ifAnyGranted>
                <br><br>
                <div class="list">
                    <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                        <thead>
                        <th>Código</th>
                        <th>Razão Social</th>
                        <th>Nome Fantasia</th>
                        <th>CNPJ</th>
                        </thead>
                        <tbody>
                            <g:each in="${Rh.list()}" status="i" var="rh">
                                <tr>
                                    <td>${rh.codigo}</td>
                                    <td>${rh.nome}</td>
                                    <td>${rh.nomeFantasia}</td>
                                    <td>${rh.cnpj}</td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>