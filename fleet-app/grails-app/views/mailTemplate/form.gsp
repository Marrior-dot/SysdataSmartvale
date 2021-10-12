<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes de Template Mail</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Templates de Email</h4>
        </div>
        <g:form action="save">
        <g:hiddenField name="id" value="${mailTemplate?.id}"></g:hiddenField>
        <div class="panel-body">
            <div class="buttons-top">
                <g:link action="index" class="btn btn-default"><i class="glyphicon glyphicon-list"></i> Listagem Templates</g:link>
            </div>


            <div class="row form-group">
                <div class="col-md-4">
                    <label>Identificação</label>
                    <g:textField name="key" class="form-control" value="${mailTemplate.key}" required=""></g:textField>
                </div>
                <div class="col-md-4">
                    <label>Nome</label>
                    <g:textField name="name" class="form-control" value="${mailTemplate.name}" required=""></g:textField>
                </div>
                <div class="col-md-4">
                    <label>Assunto</label>
                    <g:textField name="subject" class="form-control" value="${mailTemplate.subject}" required=""></g:textField>
                </div>
            </div>
            <div class="row form-group">
                <div class="col-md-4">
                    <label>Remetente</label>
                    <g:textField name="from" class="form-control" value="${mailTemplate.from}" required=""></g:textField>
                </div>
                <div class="col-md-6">
                    <label>Destinatário(s)</label>
                    <g:textField name="to" class="form-control" value="${mailTemplate.to}" required=""></g:textField>
                </div>
            </div>
            <div class="row form-group">
                <div class="col-md-12">
                    <label>Corpo Email</label>
                    <g:textArea rows="10" name="body" class="form-control" value="${mailTemplate.body}" required=""></g:textArea>
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-save"></i> Salvar</button>
        </div>
        </g:form>
    </div>
</body>
</html>