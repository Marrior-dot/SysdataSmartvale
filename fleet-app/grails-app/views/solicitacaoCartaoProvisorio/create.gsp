<%@ page import="com.sysdata.gestaofrota.StatusSolicitacaoCartaoProvisorio" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Nova Solicitação de Cartões Provisórios</title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h3>Nova Solicitação de Cartões Provisórios</h3>
    </div>
    <div class="panel-body">
        <alert:all/>
        <div class="buttons-top">
            <g:link action="index" class="btn btn-default"><i class="glyphicon glyphicon-list"></i> Listagem Solicitações</g:link>
        </div>
        <div class="panel panel-default">
            <div class="panel-body">
                <g:form action="save">
                    <div class="row form-group">
                        <div class="col-md-3">
                            <label>Quantidade</label>
                            <g:textField name="quantidade" class="form-control" value="${this.solicitacaoCartaoProvisorio.quantidade}"></g:textField>
                        </div>
                    </div>
                    <div class="buttons-top">
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-save"></i> Salvar</button>
                        <button type="reset" class="btn btn-default"><i class="glyphicon glyphicon-erase"></i> Limpar</button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>