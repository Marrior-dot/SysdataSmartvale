<%@ page import="com.sysdata.gestaofrota.Util" %>
<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#basico" aria-controls="basico" role="tab"
                                              data-toggle="tab">Dados Estabelecimento</a></li>
    <li role="presentation"><a href="#produto" aria-controls="produto" role="tab" data-toggle="tab">Produtos</a></li>
</ul>

<div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="basico">
        <g:form method="post">
            <g:hiddenField name="id" value="${estabelecimentoInstance?.id}"/>
            <g:hiddenField name="version" value="${estabelecimentoInstance?.version}"/>
            <g:hiddenField name="empresa.id" value="${empresaInstance?.id}"/>
            <g:hiddenField name="action" value="${action}"/>

            <div class="panel panel-default panel-top">
                <div class="panel-heading">
                    Dados Basicos
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-4">
                            <bs:formField id="cnpj" name="cnpj" label="CNPJ" value="${estabelecimentoInstance?.cnpj}"
                                          class="cnpj editable"/>
                        </div>

                        <div class="col-xs-6">
                            <bs:formField id="nome" name="nome" label="Razao Social"
                                          value="${estabelecimentoInstance?.nome}" class="editable"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-6">
                            <bs:formField id="nomeFantasia" name="nomeFantasia" label="Nome Fantasia"
                                          value="${estabelecimentoInstance?.nomeFantasia}" class="editable"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-4">
                            <bs:formField id="inscricaoEstadual" name="inscricaoEstadual" label="Inscrição Estadual"
                                          value="${estabelecimentoInstance?.inscricaoEstadual}"
                                          class="only-numbers editable" maxlength="10"/>
                        </div>

                        <div class="col-xs-4">
                            <bs:formField id="inscricaoMunicipal" name="inscricaoMunicipal" label="Inscrição Municipal"
                                          value="${estabelecimentoInstance?.inscricaoMunicipal}"
                                          class="only-numbers editable" maxlength="10"/>
                        </div>
                    </div>

                    <g:render template="/endereco/form"
                              model="[enderecoInstance: estabelecimentoInstance?.endereco, endereco: 'endereco', legend: 'Endereço']"/>
                    <g:render template="/telefone/form"
                              model="[telefoneInstance: estabelecimentoInstance?.telefone, telefone: 'telefone', legend: 'Telefone']"/>
                </div>
            </div>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <div class="buttons">
                    <g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
                        <button type="submit" class="btn btn-success" name="_action_${action==Util.ACTION_NEW?'save':'update'}">
                            <span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.update.label" default="Update"></g:message>
                        </button>
                    </g:if>
                    <g:if test="${action == Util.ACTION_VIEW}">
                        <button type="submit" class="btn btn-default" name="_action_edit">
                            <span class="glyphicon glyphicon-edit"></span>&nbsp;<g:message code="default.button.edit.label" default="Edit"></g:message>
                        </button>
                        <button type="submit" class="btn btn-danger" name="_action_delete"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                            <span class="glyphicon glyphicon-remove"></span>&nbsp;<g:message code="default.button.delete.label" default="Delete"></g:message>
                        </button>
                    </g:if>
                </div>
            </sec:ifAnyGranted>
        </g:form>
    </div>

    <div role="tabpanel" class="tab-pane" id="produto">
        <div id="produtosEstabelecimento">
            <g:render template="/produtoEstabelecimento/form"
                      model="[
                              produtoList               : produtoList,
                              produtoEstabelecimentoList: produtoEstabelecimentoList,
                              estabelecimento           : estabelecimentoInstance
                      ]"/>

        </div>

    </div>
</div>