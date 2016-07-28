<%@ page import="br.com.acception.greport.ParameterReport" %>

<div class="form-group">
	<div class="checkbox">
		<label for="mandatory">
			<input name="mandatory" id="mandatory" type="checkbox" value="${fieldReportInstance?.mandatory}"> <g:message code="fieldReport.mandatory.label" default="Mandatory" />
			<span class="help-block">O campo deve obrigatoriamente ser informado?</span>
		</label>
	</div>
</div>

<div class="form-group">
	<label class="control-label">Roles</label>

	<div class="form-control">
		<g:each in="${parameterReportInstance?.report?.roles?.sort{it.authority}}" var="rola">
			<label class="checkbox-inline">
				<input type="checkbox" name="roles" value="${rola.id}"
					${parameterReportInstance?.roles?.contains(rola) ? 'checked' : ''}> ${rola.authority}
			</label>
		</g:each>
	</div>
</div>

<div class="form-group ${hasErrors(bean: parameterReportInstance, field: 'dataType', 'error')} required">
	<label for="dataType">
		<g:message code="parameterReport.dataType.label" default="Data Type" />
	</label>
	<span class="required-indicator">*</span>
	<g:select class="form-control" name="dataType" from="${br.com.acception.greport.DataType?.values()}" keys="${br.com.acception.greport.DataType.values()*.name()}" required="" value="${parameterReportInstance?.dataType?.name()}"/>
</div>

<div class="form-group">
	<label for="label">
		<g:message code="parameterReport.label.label" default="Label" />

	</label>
	<g:textField class="form-control" name="label" value="${parameterReportInstance?.label}"/>
</div>


<div class="form-group ${hasErrors(bean: parameterReportInstance, field: 'markupType', 'error')} required">
	<label for="markupType">
		<g:message code="parameterReport.markupType.label" default="Markup Type" />
	</label>
	<span class="required-indicator">*</span>
	<g:select class="form-control" name="markupType" from="${br.com.acception.greport.MarkupType?.values()}" keys="${br.com.acception.greport.MarkupType.values()*.name()}" required="" value="${parameterReportInstance?.markupType?.name()}"/>
</div>

<div class="form-group ${hasErrors(bean: parameterReportInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="parameterReport.name.label" default="Name" />
		
	</label>
	<g:textField class="form-control" name="name" value="${parameterReportInstance?.name}"/>
</div>

<div class="form-group ${hasErrors(bean: parameterReportInstance, field: 'order', 'error')} required">
	<label for="order">
		<g:message code="parameterReport.order.label" default="Order" />
	</label>
	<span class="required-indicator">*</span>
	<g:field class="form-control" type="number" name="order" required="" value="${parameterReportInstance.order}"/>
</div>




