<%@ page import="br.com.acception.greport.ParameterReport" %>



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

<div class="form-group ${hasErrors(bean: parameterReportInstance, field: 'report', 'error')} required">
	<label for="report">
		<g:message code="parameterReport.report.label" default="Report" />
	</label>
	<span class="required-indicator">*</span>
	<g:select  id="report" name="report.id" from="${br.com.acception.greport.Report.list()}" optionKey="id" optionValue="name" required=""
	value="${parameterReportInstance?.report?.id}" class="many-to-one form-control"/>
</div>


<div class="form-group ${hasErrors(bean: fieldReportInstance, field: 'mandatory', 'error')} ">
	<label for="mandatory">
		<g:message code="fieldReport.mandatory.label" default="Mandatory" />
		
	</label>
	<g:checkBox name="mandatory" value="${fieldReportInstance?.mandatory}" />
</div>



