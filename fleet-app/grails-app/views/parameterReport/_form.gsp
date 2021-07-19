<%@ page import="com.sysdata.gestaofrota.Role; br.com.acception.greport.Report; br.com.acception.greport.MarkupType; br.com.acception.greport.DataType; br.com.acception.greport.ParameterReport" %>

<div class="form-group">
	<label class="control-label">Roles</label>
	<div class="form-control">
		<g:each in="${parameterReportInstance?.report?.roles?.sort{it.authority} ?: Role.list().sort{it.authority}}" var="rola">
			<label class="checkbox-inline">
				<input type="checkbox" name="roles" value="${rola.id}"
					${parameterReportInstance?.roles?.contains(rola) ? 'checked' : ''}> ${rola.authority}
			</label>
		</g:each>
	</div>
</div>

<div class="row">
    <div class="form-group col-md-6 required">
        <label for="label">
            <g:message code="parameterReport.label.label" default="Label"/>
        </label>
        <span class="required-indicator">*</span>
        <g:textField class="form-control" name="label" value="${parameterReportInstance?.label}" required=""/>
    </div>

	<div class="form-group col-md-6 required">
		<label for="name">
			<g:message code="parameterReport.name.label" default="Name" />
		</label>
		<span class="required-indicator">*</span>
		<g:textField class="form-control" name="name" value="${parameterReportInstance?.name}" required=""/>
	</div>
</div>

<div class="row">
	<div class="form-group col-md-4 required">
		<label for="dataType">
			<g:message code="parameterReport.dataType.label" default="Data Type" />
		</label>
		<span class="required-indicator">*</span>
		<g:select class="form-control" name="dataType" from="${DataType.values()}"
					optionValue="name" required="" value="${parameterReportInstance?.dataType?.name()}"/>
	</div>

	<div class="form-group col-md-4 required">
		<label for="markupType">
			<g:message code="parameterReport.markupType.label" default="Markup Type" />
		</label>
		<span class="required-indicator">*</span>
		<g:select class="form-control" name="markupType" from="${MarkupType?.values()}"
				  optionValue="name" required="" value="${parameterReportInstance?.markupType?.name()}"/>
	</div>

	<div class="form-group col-md-4 required">
		<label for="order">
			<g:message code="parameterReport.order.label" default="Order" />
		</label>
		<span class="required-indicator">*</span>
		<input type="number" class="form-control" min="1" max="100" required name="order" id="order" value="${parameterReportInstance.order}"/>
	</div>
</div>

<div class="row">
	<div class="form-group col-md-6">
		<label for="report">
			<g:message code="parameterReport.report.label" default="Report"/>
		</label>
		<g:if test="${parameterReportInstance?.report}">
			<g:hiddenField name="report.id" value="${parameterReportInstance?.report?.id}"/>
			<input type="text" id="report" class="form-control" readonly value="${parameterReportInstance.report?.name}"/>
		</g:if>
		<g:else>
			<span class="required-indicator">*</span>
			<g:select name="report.id" from="${Report.list()}" class="form-control" required=""
					  optionValue="name" optionKey="id"/>
		</g:else>
	</div>

	<div class="form-group col-md-6">
		<div class="checkbox">
			<label for="mandatory">
				<g:checkBox name="mandatory" value="${parameterReportInstance?.mandatory}"/>
				<g:message code="fieldReport.mandatory.label" default="Mandatory" />
				<span class="help-block">O campo deve ser informado obrigatoriamente?</span>
			</label>
		</div>
	</div>
</div>