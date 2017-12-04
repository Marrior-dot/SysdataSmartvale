<%@ page import="com.sysdata.gestaofrota.CategoriaFuncionario" %>
<%@ page import="com.sysdata.gestaofrota.Status" %>
<%@ page import="com.sysdata.gestaofrota.CategoriaCnh" %>

<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post">
    <g:hiddenField name="id" value="${funcionarioInstance?.id}"/>
    <g:hiddenField name="version" value="${funcionarioInstance?.version}"/>
    <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
    <g:hiddenField name="action" value="${action}"/>


    <table class="table">
        <thead>
            <tr>
                <th>${message(code: 'rh.label', default: 'RH')}</th>
                <th>${message(code: 'unidade.label', default: 'Unidade')}</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><g:link controller="rh" action="show" id="${unidadeInstance?.rh.id}">${unidadeInstance?.rh.nome}</g:link></td>
                <td><g:link controller="unidade" action="show" id="${unidadeInstance.id}">${unidadeInstance?.nome}</g:link></td>
            </tr>
        </tbody>
    </table>

    %{--dados basicos--}%
    <div class="panel panel-default">
        <div class="panel-heading">Dados Básicos</div>
        <div class="panel-body">
            <div class="row">
                <div class="form-group col-md-4">
                    <label for="matricula">Matrícula *</label>
                    <g:textField class="form-control matricula" name="matricula" value="${funcionarioInstance?.matricula}" maxlength="10" required="required"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="cpf">CPF *</label>
                    <g:textField name="cpf" value="${funcionarioInstance?.cpf}" class="form-control cpf" required="required"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="rg">RG *</label>
                    <g:textField name="rg" value="${funcionarioInstance?.rg}" maxlength="10" class="form-control only-numbers" required="required"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-6">
                    <label for="nome">Nome *</label>
                    <div class="input-group">
                        <g:textField name="nome" value="${funcionarioInstance?.nome}" maxlength="50" class="form-control" required="required"/>
                        <span class="input-group-addon">
                            <g:checkBox name="gestor" value="${funcionarioInstance?.gestor}"/> Gestor
                        </span>
                    </div>
                </div>

                <div class="form-group col-md-3">
                    <label for="dataNascimento">Data Nascimento *</label>
                    <input type="text" class="form-control date" id="dataNascimento" name="dataNascimento" required
                           value="${Util.formattedDate(funcionarioInstance?.dataNascimento)}"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="cnh">CNH *</label>
                    <g:textField name="cnh" class="form-control only-numbers" value="${funcionarioInstance?.cnh}" maxlength="11" required="required"/>
                </div>
            </div>

            <g:if test="${unidadeInstance?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.FUNCIONARIO}">
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="nomeEmbossing">Nome Impresso no Cartão *</label>
                        <input type="text" class="form-control" id="nomeEmbossing" name="nomeEmbossing" maxlength="${tamMaxEmbossing}"
                               placeholder="Digite aqui o nome que será impresso no seu cartão." value="${funcionarioInstance?.nomeEmbossing}" required/>
                        <span id="helpBlock" class="help-block">O campo acima pode conter no máximo <strong id="tam-max-embossing-str">${tamMaxEmbossing}</strong> caracteres.</span>
                    </div>

                    <div class="form-group col-md-3">
                        <label for="portador.valorLimite">Limite *</label>
                        <div class="input-group">
                            <span class="input-group-addon">R$</span>
                            <input type="number" min="0" step="0.01" class="form-control"
                                   id="portador.valorLimite" name="portador.valorLimite"
                                   value="${funcionarioInstance?.portador?.valorLimite}" required/>
                        </div>
                    </div>

                    <div class="form-group col-md-3">
                        <label for="portador.tipoLimite">Tipo Limite *</label>
                        <g:select name="portador.tipoLimite" from="${com.sysdata.gestaofrota.TipoLimite.values()}"
                                  required="required" value="${funcionarioInstance?.portador?.tipoLimite}"
                                  class="form-control" optionValue="nome"/>
                    </div>
                </div>
            </g:if>

            <div class="row">
                <div class="form-group col-md-3">
                    <label for="validadeCnh">Validade CNH *</label>
                    <input type="text" class="form-control datepicker" id="validadeCnh" name="validadeCnh" required
                           value="${Util.formattedDate(funcionarioInstance?.validadeCnh)}"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="categoriaCnh">Categoria CNH *</label>
                    <g:select name="categoriaCnh" from="${CategoriaCnh.values()}"
                              optionValue="nome" class="form-control" required="required"
                              noSelection="${['null': 'Selecione uma Categ. CNH...']}"
                              value="${funcionarioInstance?.categoriaCnh}"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="categoria.id">Categoria *</label>
                    <g:select name="categoria.id" from="${CategoriaFuncionario.porUnidade(unidadeInstance).list()}"
                              value="${funcionarioInstance?.categoria?.id}" required="required"
                              noSelection="${['null': 'Selecione a categoria...']}"
                              optionKey="id" class="form-control" optionValue="nome"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="status">Status *</label>
                    <g:select name="status" from="${Status.asBloqueado()}" class="form-control" required="required"
                              value="${funcionarioInstance?.status}" optionKey="key"/>
                </div>
            </div>
        </div>
    </div>

    <g:render template="/endereco/form" model="[enderecoInstance: funcionarioInstance?.endereco, endereco: 'endereco', legend: 'Endereço Residencial']"/>

    <g:render template="/telefone/form" model="[telefoneInstance: funcionarioInstance?.telefone, telefone: 'telefone', legend: 'Telefone Residencial']"/>
    <g:render template="/telefone/form" model="[telefoneInstance: funcionarioInstance?.telefoneComercial, telefone: 'telefoneComercial', legend: 'Telefone Comercial', required: true]"/>
    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">
        <div class="buttons">
            <g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
                <g:actionSubmit class="btn btn-default" action="${action == Util.ACTION_NEW ? 'save' : 'update'}"
                                value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            </g:if>
            <g:if test="${action == Util.ACTION_VIEW}">
                <g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
                <g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </g:if>
        </div>
    </sec:ifAnyGranted>

</g:form>
