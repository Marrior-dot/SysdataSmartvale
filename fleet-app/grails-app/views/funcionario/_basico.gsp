<%@ page import="com.sysdata.gestaofrota.TipoVinculoCartao; com.sysdata.gestaofrota.TipoCobranca; com.sysdata.gestaofrota.CategoriaFuncionario" %>
<%@ page import="com.sysdata.gestaofrota.Status" %>
<%@ page import="com.sysdata.gestaofrota.CategoriaCnh" %>

<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post">
    <g:hiddenField name="id" value="${funcionarioInstance?.id}"/>
    <g:hiddenField name="version" value="${funcionarioInstance?.version}"/>
    <g:hiddenField name="unidade.id" value="${funcionarioInstance?.unidade?.id}"/>

    <g:if test="${funcionarioInstance?.unidade?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.FUNCIONARIO}">
        <g:hiddenField name="portador.unidade.id" value="${funcionarioInstance?.portador?.unidade?.id}"/>
    </g:if>

    <g:hiddenField name="action" value="${action}"/>


%{--dados basicos--}%
    <div class="panel panel-default">
        <div class="panel-heading">Dados Básicos</div>

        <div class="panel-body">
            <div class="row">
                <div class="form-group col-md-4">
                    <label for="matricula">Matrícula *</label>
                    <input type="text" minlength="10" maxlength="10" class="form-control matricula editable" name="matricula"
                           id="matricula"
                           value="${funcionarioInstance?.matricula}" required>
                </div>

                <div class="form-group col-md-4">
                    <label for="cpf">CPF *</label>
                    <g:textField name="cpf" value="${funcionarioInstance?.cpf}" class="form-control cpf editable"
                                 minlength="14" required="required"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="rg">RG *</label>
                    <g:textField name="rg" value="${funcionarioInstance?.rg}" maxlength="10"
                                 class="form-control only-numbers editable" required="required"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-6">
                    <label for="nome">Nome *</label>

                        <g:textField name="nome" value="${funcionarioInstance?.nome}" maxlength="50"
                                     class="form-control editable" required="required"/>
%{--
                        <span class="input-group-addon">
                            <g:checkBox name="gestor" value="${funcionarioInstance?.gestor}"/> Gestor
                        </span>
--}%
                </div>

                <div class="form-group col-md-6">
                    <label for="email">E-Mail *</label>
                    <g:textField name="email" value="${funcionarioInstance?.email}" maxlength="50" class="form-control editable" />

                </div>


            </div>

            <div class="row">


                <div class="form-group col-md-3">
                    <label for="dataNascimento">Data Nascimento *</label>
                    <input type="text" class="form-control datepicker editable" id="dataNascimento" name="dataNascimento"
                           minlength="8" required
                           value="${Util.formattedDate(funcionarioInstance?.dataNascimento)}"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="cnh">CNH *</label>
                    <g:textField name="cnh" class="form-control only-numbers editable" value="${funcionarioInstance?.cnh}"
                                 maxlength="11" required="required"/>
                </div>
            </div>

            <g:if test="${funcionarioInstance?.unidade?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.FUNCIONARIO}">
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="nomeEmbossing">Nome Impresso no Cartão *</label>
                        <input type="text" list="nomeEmbossing-itens" autocomplete="off"
                               class="form-control editable" id="nomeEmbossing" name="nomeEmbossing"
                               maxlength="${tamMaxEmbossing}"
                               placeholder="Digite aqui o nome que será impresso no seu cartão."
                               value="${funcionarioInstance?.nomeEmbossing}" required>
                        <datalist id="nomeEmbossing-itens">
                        </datalist>

                        <span id="helpBlock" class="help-block">O campo acima pode conter no máximo <strong
                                id="tam-max-embossing-str">${tamMaxEmbossing}</strong> caracteres.</span>
                    </div>
                </div>

            </g:if>

            <div class="row">
                <div class="form-group col-md-3">
                    <label for="validadeCnh">Validade CNH *</label>
                    <input type="text" class="form-control datepicker editable" id="validadeCnh" name="validadeCnh" required
                           value="${Util.formattedDate(funcionarioInstance?.validadeCnh)}"/>
                </div>

                <div class="form-group col-md-3">
                    <label for="categoriaCnh">Categoria CNH *</label>
                    <g:select name="categoriaCnh" from="${CategoriaCnh.values()}"
                              optionValue="nome" class="form-control editable" required="required"
                              noSelection="${['null': 'Selecione uma Categ. CNH...']}"
                              value="${funcionarioInstance?.categoriaCnh}"/>
                </div>

                <g:if test="${funcionarioInstance?.unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO &&
                        funcionarioInstance?.unidade.rh.modeloCobranca == TipoCobranca.PRE_PAGO }">
                    <div class="form-group col-md-3">
                        <label for="categoria.id">Perfil de Recarga *</label>
                        <g:select name="categoria.id" from="${CategoriaFuncionario.porUnidade(funcionarioInstance?.unidade)?.list()}"
                                  value="${funcionarioInstance?.categoria?.id}" required="required"
                                  noSelection="${['null': 'Selecione a categoria...']}"
                                  class="editable"
                                  optionKey="id" class="form-control" optionValue="nome"/>
                    </div>

                </g:if>


%{--
                <div class="form-group col-md-3">
                    <label for="status">Status *</label>
                    <g:select name="status" from="${Status.asBloqueado()}" class="form-control" required="required"
                              value="${funcionarioInstance?.status}" optionKey="key"/>
                </div>
--}%
            </div>
        </div>
    </div>


    <g:if test="${funcionarioInstance?.unidade?.rh?.vinculoCartao == com.sysdata.gestaofrota.TipoVinculoCartao.FUNCIONARIO &&
            funcionarioInstance?.unidade?.rh?.modeloCobranca == TipoCobranca.POS_PAGO}">

        <div class="panel panel-default">
            <div class="panel-heading">Limites</div>

            <div class="panel-body">
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="portador.limiteTotal">Limite Total *</label>
                        <g:textField name="portador.limiteTotal" class="form-control money editable"
                                     value="${Util.formatCurrency(funcionarioInstance?.portador?.limiteTotal)}" required="true"></g:textField>

                    </div>

%{--
                    <div class="form-group col-md-3">
                        <label for="portador.limiteDiario">Limite Diário *</label>
                        <input class="form-control money" id="portador.limiteDiario" name="portador.limiteDiario"
                               value="${funcionarioInstance?.portador?.limiteDiario}"/>
                    </div>

                    <div class="form-group col-md-3">
                        <label for="portador.limiteMensal">Limite Mensal *</label>
                        <input class="form-control money" id="portador.limiteMensal" name="portador.limiteMensal"
                               value="${funcionarioInstance?.portador?.limiteMensal}"/>
                    </div>
--}%
                </div>
            </div>
        </div>
    </g:if>



    <g:render template="/endereco/form"
              model="[enderecoInstance: funcionarioInstance?.endereco, endereco: 'endereco', legend: 'Endereço Residencial']"/>

    <g:render template="/telefone/form"
              model="[telefoneInstance: funcionarioInstance?.telefone, telefone: 'telefone', className: 'cell-phone', legend: 'Telefone Pessoal']"/>
    <g:render template="/telefone/form"
              model="[telefoneInstance: funcionarioInstance?.telefoneComercial,
                      telefone: 'telefoneComercial', legend: 'Telefone Comercial', required: true]"/>

    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC, ROLE_RH">

        <div class="panel-footer">
            <g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
                <button type="submit" class="btn btn-success"
                        name="_action_${action == Util.ACTION_NEW ? 'save' : 'update'}">
                    <span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.update.label"
                                                                                   default="Update"></g:message>
                </button>
            </g:if>
            <g:if test="${action == Util.ACTION_VIEW}">

                <button type="submit" class="btn btn-default" name="_action_edit">
                    <span class="glyphicon glyphicon-edit"></span>&nbsp;<g:message
                        code="default.button.edit.label.label" default="Editar"></g:message>
                </button>

                <button type="submit" class="btn btn-danger" name="_action_delete"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Você tem certeza?')}');">
                    <span class="glyphicon glyphicon-remove"></span>&nbsp;<g:message code="default.button.delete.label"
                                                                                     default="Delete"></g:message>
                </button>

            </g:if>
        </div>

    </sec:ifAnyGranted>

</g:form>


<script>
    $(function () {
        $('#nome').blur(function () {

            $.ajax({
                url: "/GestaoFrota/funcionario/sugestoes.json",
                data: {nome: this.value},
                dataType: "json"
            }).done(function (data) {
                $('#nomeEmbossing-itens option').remove()
                jQuery.each(data.sug, function (index, value) {
                    $('#nomeEmbossing-itens').append('<option value="' + value + '">' + value + '</option>');
                });
            });
        });
    });
</script>