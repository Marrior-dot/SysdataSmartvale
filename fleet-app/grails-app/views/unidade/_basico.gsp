<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post">
    <g:hiddenField name="id" value="${unidadeInstance?.id}" />
    <g:hiddenField name="version" value="${unidadeInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>
    <g:hiddenField name="rhId" value="${rhInstance?.id}"/>

	<div class="panel panel-default panel-top">
		<div class="panel-heading">Dados Básicos</div>

		<div class="panel-body">
            <div class="row">
                <g:if test="${action == Util.ACTION_VIEW}">
                    <div class="col-md-2">
                        <bs:formField id="codigo" name="codigo" label="Código" value="${unidadeInstance?.codigo}" />
                    </div>
                </g:if>

                <div class="col-md-4">
                    <bs:formField id="nome" name="nome" label="Nome" class="editable" value="${unidadeInstance?.nome}" />
                </div>

                <div class="col-md-6">
                    <bs:formField id="nomeEmbossing" name="nomeEmbossing" label="Nome Unidade p/ Embossing" class="editable"
                                    maxlength="${grailsApplication.config.projeto.cartao.embossing.maximoColunasLinhaEmbossing}"
                                    value="${unidadeInstance?.nomeEmbossing}" />
                </div>
            </div>
			<div class="panel-footer">
				<g:if test="${action in [Util.ACTION_NEW, Util.ACTION_EDIT]}">
                    <button type="submit" class="btn btn-success" name="_action_${action==Util.ACTION_NEW?'save':'update'}" >
                        <span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.create.label" default="Create" ></g:message>
                    </button>
                </g:if>
</g:form>
<g:form  controller="unidade" action="delete" id="${unidadeInstance?.id}" method="delete">

    <g:if test="${action==Util.ACTION_VIEW}">
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                <g:link type="button" class="btn btn-default" action="edit" resource="${unidadeInstance}" id="${unidadeInstance?.id}">
                    <span class="glyphicon glyphicon-edit"></span>
                    <g:message code="default.button.edit.label" default="Edit"/>
                </g:link>


            <button type="submit" class="btn btn-danger"
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                <span class="glyphicon glyphicon-remove"></span>
                <g:message code="default.button.delete.label"/>
            </button>
        </sec:ifAnyGranted>
            </g:if>

</g:form>

        </div>
    </div>
</div>



