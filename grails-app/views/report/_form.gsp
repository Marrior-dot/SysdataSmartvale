<%@ page import="br.com.acception.greport.Report" %>



<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'name', 'error')} ">
	<label for="countQuery">
		<g:message code="report.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${reportInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'queryType', 'error')} required">
	<label for="queryType">
		<g:message code="report.queryType.label" default="Query Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="queryType" from="${br.com.acception.greport.QueryType?.values()}" keys="${br.com.acception.greport.QueryType.values()*.name()}" required="" value="${reportInstance?.queryType?.name()}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'query', 'error')} ">
	<label for="query">
		<g:message code="report.query.label" default="Query" />
		
	</label>
	<g:textArea name="query" value="${reportInstance?.query}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'countQuery', 'error')} ">
	<label for="countQuery">
		<g:message code="report.countQuery.label" default="Count Query" />
		
	</label>
	<g:textArea name="countQuery" value="${reportInstance?.countQuery}"/>
	
</div>

<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'fields', 'error')} ">
	<label for="fields">
		<g:message code="report.fields.label" default="Fields" />
		
	</label>


<table>
	
	<g:each in="${reportInstance?.fields?}" var="f">
	    <tr><g:link controller="fieldReport" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></tr>
	</g:each>
</table>
	
<g:link controller="fieldReport" action="create" params="['report.id': reportInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'fieldReport.label', default: 'FieldReport')])}</g:link>

</div>

<div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'parameters', 'error')} ">
	<label for="parameters">
		<g:message code="report.parameters.label" default="Parameters" />
		
	</label>
	
	<table>
	
		<g:each in="${reportInstance?.parameters?}" var="p">
			<tr>
			<g:link controller="parameterReport" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link>
			</tr>
		</g:each>
	</table>
	
<g:link controller="parameterReport" action="create" params="['report.id': reportInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'parameterReport.label', default: 'ParameterReport')])}</g:link>	

</div>



