
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
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
            <div class="buttons-top">
                <g:link action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" />
                </g:link>
            </div>
            </sec:ifAnyGranted>
                <div class="panel-body">
                    <div class="panel panel-default">
                        <div class="panel-heading">Filtros</div>
                        <div class="panel-body">
                            <g:form action="list">
                                <div class="row">
                                    <div class="form-group col-md-3">
                                        <label class="control-label">CNPJ</label>
                                        <g:textField class="form-control" name="cnpj" value="${params.cnpj}"></g:textField>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label class="control-label">Razão Social</label>
                                        <g:textField class="form-control" name="razao" value="${params.razao}"></g:textField>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <button type="submit" class="btn btn-default">
                                            <i class="glyphicon glyphicon-search"></i> Pesquisar
                                        </button>
                                    </div>
                                </div>
                            </g:form>
                        </div>
                    </div>

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