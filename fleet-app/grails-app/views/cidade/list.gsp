<%@ page import="com.sysdata.gestaofrota.Estado; com.sysdata.gestaofrota.Cidade" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'cidade.label', default: 'Cidade')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default panel-top">
            <div class="panel panel-heading">
                <h4>Lista de Cidades</h4>
            </div>
            <div class="panel-body">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        Pesquisa
                    </div>

                    <div class="panel-body">

                        <div class="row">

                            <g:form action="index">
                                <div class="col-md-3">
                                    <label>Estado</label>
                                    <g:select name="estado" class="form-control" from="${Estado.list(sort: 'uf')}"
                                              optionKey="id"
                                              optionValue="nome"
                                              value="${params.estado}"
                                              noSelection="['': '-- Todos --']"
                                    >
                                    </g:select>

                                </div>
                                <div class="col-md-3">
                                    <button type="submit" class="btn btn-default" style="margin-top: 20px;"><i class="glyphicon glyphicon-search"></i>&nbsp;Pesquisar</button>
                                </div>

                            </g:form>

                        </div>
                    </div>
                </div>

                <table class="table table-stripped">
                    <thead>
                    <th>#</th>
                    <th>Nome</th>
                    <th>UF</th>
                    </thead>
                    <tbody>
                    <g:each in="${cidadeInstanceList}" var="cid" >
                        <tr>
                            <td><g:link action="show" id="${cid.id}">${cid.id}</g:link></td>
                            <td>${cid.nome}</td>
                            <td>${cid.estado.uf}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:paginate total="${cidadeInstanceTotal}"></g:paginate>

            </div>
        </div>
    </body>
</html>
