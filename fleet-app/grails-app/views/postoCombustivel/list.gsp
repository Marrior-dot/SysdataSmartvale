
<%@ page import="com.sysdata.gestaofrota.Equipamento; com.sysdata.gestaofrota.Rh" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="layout-restrito" />
    <g:set var="entityName" value="Credenciado" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">

            <alert:all/>


            <div class="panel panel-default">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <div class="panel-heading">
                    <g:link action="create" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus"></span>
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link>
                </div>
            </sec:ifAnyGranted>
                <div class="panel-body">

                    <table class="table table-striped">

                        <thead>
                        <th>CNPJ</th>
                        <th>Razão Social</th>
                        <th>Nome Fantasia</th>
                        </thead>
                        <tbody>
                        <g:each in="${empresasList}" var="emp">
                            <tr>
                                <td><g:link action="show" id="${emp.id}">${emp.cnpj}</g:link></td>
                                <td>${emp.nome}</td>
                                <td>${emp.nomeFantasia}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:paginate total="${empresasCount}" params="${params}"></g:paginate>
                </div>
            </div>
        </div>
    </div>
</body>
</html>