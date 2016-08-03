<%@ page import="com.sysdata.gestaofrota.Util" %>

<br/>
<g:form method="post" >
    <g:hiddenField name="id" value="${estabelecimentoInstance?.id}" />
    <g:hiddenField name="version" value="${estabelecimentoInstance?.version}" />
    <g:hiddenField name="empId" value="${empresaInstance?.id}" />
    <g:hiddenField name="action" value="${action}"/>


    <div class="panel panel-default">
        <div class="panel-heading">
            Dados Basicos
        </div>

        <div class="panel-body">

            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="cnpj" name="cnpj" label="CNPJ"  value="${estabelecimentoInstance?.cnpj}" class="cnpj"/>
                </div>
                <div class="col-xs-6">
                    <bs:formField id="nome" name="nome" label="Razao Social"  value="${estabelecimentoInstance?.nome}" />
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <bs:formField id="nomeFantasia" name="nomeFantasia" label="Nome Fantasia"  value="${estabelecimentoInstance?.nomeFantasia}" />
                </div>

            </div>

            <div class="row">
                <div class="col-xs-4">
                    <bs:formField id="inscricaoEstadual" name="inscricaoEstadual" label="Inscrição Estadual" value="${estabelecimentoInstance?.inscricaoEstadual}" class="only-numbers" maxlength="10"/>
                </div>
                <div class="col-xs-4">
                    <bs:formField id="inscricaoMunicipal" name="inscricaoMunicipal" label="Inscrição Municipal" value="${estabelecimentoInstance?.inscricaoMunicipal}" class="only-numbers" maxlength="10"/>
                </div>
            </div>


            <g:render template="/endereco/form" model="[enderecoInstance:estabelecimentoInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>

            <g:render template="/telefone/form" model="[telefoneInstance:estabelecimentoInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>

        </div>


    </div>
    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
        <div class="buttons">
            <g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
                <g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}"
                                value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            </g:if>
            <g:if test="${action==Util.ACTION_VIEW}">
                <g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
                <g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </g:if>
        </div>
    </sec:ifAnyGranted>


</g:form>


