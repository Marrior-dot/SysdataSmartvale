<%@ page import="com.sysdata.gestaofrota.Util" %>

<g:form method="post">
    <g:hiddenField name="id" value="${unidadeInstance?.id}" />
    <g:hiddenField name="version" value="${unidadeInstance?.version}" />
    <g:hiddenField name="action" value="${action}"/>
    <g:hiddenField name="rhId" value="${rhInstance?.id}"/>

	<div class="panel panel-default">
		<div class="panel-heading">Dados Básicos</div>

		<div class="panel-body">

            <div class="row">
                <g:if test="${action == Util.ACTION_VIEW}">
                    <div class="col-xs-2">
                        <bs:formField id="id" name="id" label="Código" class="id" value="${unidadeInstance?.id}" />
                    </div>
                </g:if>

                <div class="col-xs-4">
                    <bs:formField id="nome" name="nome" label="Nome" class="nome" value="${unidadeInstance?.nome}" />
                </div>

            </div>


			<fieldset class="buttons">
				<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
                    <g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}"
                                    value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    %{--<button type="submit" class="btn btn-default" >
                        <span class="glyphicon glyphicon-floppy-save"></span>
                        <g:message code="default.button.update.label"/>
                    </button>--}%
                </g:if>
</g:form>
<g:form  controller="unidade" action="delete" id="${unidadeInstance?.id}" method="delete">

    <g:if test="${action==Util.ACTION_VIEW}">
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PROC">
                <g:link type="button" class="btn btn-default" action="edit" resource="${unidadeInstance}" id="${unidadeInstance?.id}">
                    <span class="glyphicon glyphicon-edit"></span>
                    <g:message code="default.button.edit.label" default="Edit"/>
                </g:link>


            <button type="submit" class="btn btn-default"  onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                <span class="glyphicon glyphicon-floppy-remove"></span>
                <g:message code="default.button.delete.label"/>
            </button>
        </sec:ifAnyGranted>
            </g:if>

</g:form>

        </fieldset>
    </div>
</div>



