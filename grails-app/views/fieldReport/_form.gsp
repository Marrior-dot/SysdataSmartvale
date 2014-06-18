<%@ page import="br.com.acception.greport.FieldReport" %>



<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'dataType', 'error')} required">
	<label for="dataType">
		<g:message code="fieldReport.dataType.label" default="Data Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="dataType" from="${br.com.acception.greport.DataType?.values()}" keys="${br.com.acception.greport.DataType.values()*.name()}" required="" value="${fieldReportInstance?.dataType?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'groupBy', 'error')} ">
	<label for="groupBy">
		<g:message code="fieldReport.groupBy.label" default="Group By" />
		
	</label>
	<g:checkBox name="groupBy" value="${fieldReportInstance?.groupBy}" />
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'label', 'error')} ">
	<label for="label">
		<g:message code="fieldReport.label.label" default="Label" />
		
	</label>
	<g:textField name="label" value="${fieldReportInstance?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="fieldReport.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${fieldReportInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'order', 'error')} required">
	<label for="order">
		<g:message code="fieldReport.order.label" default="Order" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="order" required="" value="${fieldReportInstance.order}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'report', 'error')} required">
	<label for="report">
		<g:message code="fieldReport.report.label" default="Report" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="report" name="report.id" from="${br.com.acception.greport.Report.list()}" optionKey="id" required="" value="${fieldReportInstance?.report?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fieldReportInstance, field: 'totalizer', 'error')} ">
	<label for="totalizer">
		<g:message code="fieldReport.totalizer.label" default="Totalizer" />
		
	</label>
	<g:checkBox name="totalizer" value="${fieldReportInstance?.totalizer}" />
</div>
