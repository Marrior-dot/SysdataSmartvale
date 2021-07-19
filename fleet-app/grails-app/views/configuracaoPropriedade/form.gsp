<%@ page import="com.sysdata.gestaofrota.TipoParticipante" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8" >
    <title></title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4>Configuração de Propriedade</h4>
    </div>

    <g:form method="POST">
        <g:hiddenField name="id" value="${configuracaoPropriedade?.id}" />

        <div class="panel-body">

            <g:link action="index" class="btn btn-default" ><span class="glyphicon glyphicon-list"></span>&nbsp;Listar Configurações</g:link>

            <br/>
            <br/>

            <div class="row">
                <div class="col-md-3">
                    <label for="nome">Nome</label>
                    <g:textField name="nome" class="form-control" value="${configuracaoPropriedade?.nome}"></g:textField>
                </div>
                <div class="col-md-3">
                    <label for="tipoParticipante">Tipo Participante</label>
                    <g:select name="tipoParticipante" class="form-control" from="${TipoParticipante.values()}" optionValue="nome"
                              noSelection="['': '--Selecione o Tipo Participante']" value="${configuracaoPropriedade?.tipoParticipante}"></g:select>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <label for="ativo">Ativo</label>
                    <g:checkBox name="ativo" value="${configuracaoPropriedade?.ativo}"></g:checkBox>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <label for="dsl">DSL</label>
                    <g:textArea name="dsl" class="form-control" rows="10" value="${configuracaoPropriedade?.dsl}">

                    </g:textArea>
                </div>
            </div>
        </div>

        <div class="panel-footer">

            <g:if test="${state == 'edit' || state == 'new'}">
                <button type="submit" name="_action_save" class="btn btn-success">
                    <span class="glyphicon glyphicon-save"></span> Salvar</button>

            </g:if>
            <g:if test="${state == 'view'}">
                <button type="submit" name="_action_edit" class="btn btn-default">
                    <span class="glyphicon glyphicon-edit"></span> Editar</button>

                <button type="submit" name="_action_delete" class="btn btn-danger">
                    <span class="glyphicon glyphicon-trash"></span> Remover</button>

            </g:if>

        </div>

    </g:form>

</div>
</body>
</html>