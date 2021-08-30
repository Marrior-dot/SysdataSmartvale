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
            <g:link action="index" class="btn btn-default"><i class="glyphicon glyphicon-th-list"></i> Listagem Solicitações</g:link>
            <g:link action="create" class="btn btn-default"><i class="glyphicon glyphicon-plus"></i> Nova Solicitação</g:link>
        </div>
        <div class="panel panel-default">
            <table class="table table-stripped">
                <tbody>
                <tr>
                    <th>Data/Hora</th>
                    <td><g:formatDate date="${this.solicitacaoCartaoProvisorio.dateCreated}"
                                      format="dd/MM/yyyy HH:mm"></g:formatDate></td>
                </tr>
                <tr>
                    <th>Quantidade</th>
                    <td>${this.solicitacaoCartaoProvisorio.quantidade}</td>
                </tr>
                <tr>
                    <th>Solicitante</th>
                    <td>${this.solicitacaoCartaoProvisorio.solicitante.name}</td>
                </tr>
                <tr>
                    <th>Status</th>
                    <td>${this.solicitacaoCartaoProvisorio.status.nome}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="buttons-top">
            <g:form>
                <g:hiddenField name="id" value="${this.solicitacaoCartaoProvisorio.id}"></g:hiddenField>
                <button name="_action_cancel" type="submit" class="btn btn-danger" >
                    <i class="glyphicon glyphicon-remove"></i> Cancelar</button>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>