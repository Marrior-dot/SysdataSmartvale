<%@ page import="com.sysdata.gestaofrota.StatusSolicitacaoCartaoProvisorio" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Solicitações de Cartões Provisórios</title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>Solicitações de Cartões Provisórios</h4>
    </div>
    <div class="panel-body">
        <alert:all/>
        <div class="buttons-top">
            <g:link action="create" class="btn btn-default"><i class="glyphicon glyphicon-plus"></i> Nova Solicitação</g:link>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h5>Filtros</h5>
            </div>
            <div class="panel-body">
                <g:form>
                    <div class="row form-group">
                        <div class="col-md-3">
                            <label>Status</label>
                            <g:select name="status" class="form-control" from="${StatusSolicitacaoCartaoProvisorio.values()}"
                                      optionValue="nome" value="${params.status}"></g:select>
                        </div>
                        <div class="col-md-3">
                            <button type="submit" class="btn btn-default">
                                <i class="glyphicon glyphicon-search"></i> Pesquisar
                            </button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
        <table class="table table-stripped">
            <thead>
            <th>Data/Hora</th>
            <th>Quantidade</th>
            <th>Solicitante</th>
            <th>Status</th>
            </thead>
            <tbody>
            <g:each in="${solicitacaoList}" var="sol">
                <tr>
                    <td>
                        <g:link action="show" id="${sol.id}">
                            <g:formatDate date="${sol.dateCreated}" format="dd/MM/yy HH:mm"></g:formatDate>
                        </g:link>
                    </td>
                    <td>${sol.quantidade}</td>
                    <td>${sol.solicitante.name}</td>
                    <td>${sol.status.nome}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <g:paginate total="${solicitacaoCount}" params="${params}"></g:paginate>
    </div>
</div>
</body>
</html>