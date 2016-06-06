<%@ page import="com.sysdata.gestaofrota.CategoriaFuncionario" %>
<%@ page import="com.sysdata.gestaofrota.Status" %>
<%@ page import="com.sysdata.gestaofrota.CategoriaCnh" %>

<%@ page import="com.sysdata.gestaofrota.Util" %>




<g:form method="post">
    <g:hiddenField name="id" value="${funcionarioInstance?.id}"/>
    <g:hiddenField name="version" value="${funcionarioInstance?.version}"/>
    <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
    <g:hiddenField name="action" value="${action}"/>


    <table class="table" style="border:1px solid;border-color: #DDD;font-size:14px;">
        <thead>
            <tr>
                <th>${message(code: 'rh.label', default: 'RH')}</th>
                <th>${message(code: 'unidade.label', default: 'Unidade')}</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${unidadeInstance?.rh.nome}</td>
                <td>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</td>
            </tr>
        </tbody>
    </table>

    <div class="panel panel-default">
        <div class="panel-heading">Dados Básicos</div>
        <div class="panel-body">
            <div class="row">
                <div class="form-group col-md-4">
                    <label for="matricula">Matrícula</label>
                    <g:textField class="form-control matricula" name="matricula" value="${funcionarioInstance?.matricula}" maxlength="10"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="cpf">CPF</label>
                    <g:textField name="cpf" value="${funcionarioInstance?.cpf}" class="form-control cpf"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="rg">RG</label>
                    <g:textField name="rg" value="${funcionarioInstance?.rg}" maxlength="10" class="form-control only-numbers"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-8">
                    <label for="nome">Nome</label>
                    <g:textField name="nome" value="${funcionarioInstance?.nome}" maxlength="50" class="form-control"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="dataNascimento">Data Nascimento</label>
                    <input type="text" class="form-control date" id="dataNascimento" name="dataNascimento"
                           value="${funcionarioInstance?.dataNascimento}"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-4">
                    <label for="cnh">CNH</label>
                    <g:textField name="cnh" class="form-control only-numbers" value="${funcionarioInstance?.cnh}" maxlength="11"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="validadeCnh">Validade CNH</label>
                    <input type="text" class="form-control datepicker" id="validadeCnh" name="validadeCnh" value="${funcionarioInstance?.cnh}"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="categoria.id">Categoria CNH</label>
                    <g:select name="categoria.id" value="${funcionarioInstance?.categoria?.id}"
                              noSelection="${['null': 'Selecione a categoria...']}" class="form-control"
                              from="${CategoriaFuncionario.withCriteria {
                                  rh { eq('id', unidadeInstance?.rh?.id) }
                              }}"
                              optionKey="id" optionValue="nome"/>
                </div>
            </div>
        </div>
    </div>



        <fieldset class="uppercase">
            <h2>Dados Básicos</h2>



            <div>

                <label><span>Categoria</span><g:select name="categoria.id"
                                                       value="${funcionarioInstance?.categoria?.id}"
                                                       noSelection="${['null': 'Selecione a categoria...']}"
                                                       from="${CategoriaFuncionario.withCriteria {
                                                           rh { eq('id', unidadeInstance?.rh?.id) }
                                                       }}"
                                                       optionKey="id"
                                                       optionValue="nome"></g:select></label>

                <label id="lblGestor"><span>Gestor</span><g:checkBox name="gestor"
                                                                     value="${funcionarioInstance?.gestor}"/></label>

            </div>

            <div>

                <label><span>Status</span><g:select name="status" from="${Status.asBloqueado()}"
                                                    value="${funcionarioInstance?.status}" optionKey=""
                                                    optionValue=""></g:select></label>

            </div>

        </fieldset>

        <g:render template="/endereco/form"
                  model="[enderecoInstance: funcionarioInstance?.endereco, endereco: 'endereco', legend: 'Endereço Residencial']"/>

        <div style="float:left">
            <g:render template="/telefone/form"
                      model="[telefoneInstance: funcionarioInstance?.telefone, telefone: 'telefone', legend: 'Telefone Residencial']"/>
        </div>
        <g:render template="/telefone/form"
                  model="[telefoneInstance: funcionarioInstance?.telefoneComercial, telefone: 'telefoneComercial', legend: 'Telefone Comercial']"/>

    <div class="buttons">
        <g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
            <span class="button"><g:actionSubmit class="save" action="${action == Util.ACTION_NEW ? 'save' : 'update'}"
                                                 value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
        </g:if>
        <g:if test="${action == Util.ACTION_VIEW}">
            <span class="button"><g:actionSubmit class="edit" action="edit"
                                                 value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete"
                                                 value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                 onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
        </g:if>
    </div>

</g:form>
