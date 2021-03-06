<%@ page import="com.sysdata.gestaofrota.Role; br.com.acception.greport.QueryType; br.com.acception.greport.Report" %>
<div class="row">
    <div class="form-group col-md-6 ${hasErrors(bean: reportInstance, field: 'queryType', 'error')} required">
        <label class="control-label" for="queryType">
            <g:message code="report.queryType.label" default="Query Type"/>
        </label>
        <span class="required-indicator">*</span>
        <g:select class="form-control" name="queryType" from="${br.com.acception.greport.QueryType?.values()}"
                  keys="${QueryType.values()*.name()}" required="" value="${reportInstance?.queryType?.name()}"/>
    </div>

    <div class="form-group col-md-6 ${hasErrors(bean: reportInstance, field: 'name', 'error')} ">
        <label class="control-label" for="name"><g:message code="report.name.label" default="Name"/></label>
        <input class="form-control" id="name" name="name" value="${reportInstance?.name}"/>
    </div>

    <div class="form-group col-md-12">
        <label class="control-label">Roles</label>

        <div class="form-control">
            <g:each in="${Role.list().sort { it.authority }}" var="rola">
                <label class="checkbox-inline">
                    <input type="checkbox" name="roles" value="${rola.id}"
                        ${reportInstance?.roles?.contains(rola) ? 'checked' : ''}> ${rola.authority}
                </label>
            </g:each>
        </div>
    </div>
</div>

<div class="row">
    <div class="form-group col-md-6 ${hasErrors(bean: reportInstance, field: 'query', 'error')} ">
        <label for="query"><g:message code="report.query.label" default="Query"/></label>
        <g:textArea rows="20" class="form-control" id="query" name="query" value="${reportInstance?.query}"/>
    </div>

    <div class="form-group col-md-6 ${hasErrors(bean: reportInstance, field: 'countQuery', 'error')} ">
        <label class="control-label" for="countQuery">
            <g:message code="report.countQuery.label" default="Count Query"/>
        </label>
        <g:textArea rows="20" class="form-control" name="countQuery" value="${reportInstance?.countQuery}"/>
    </div>
</div>

<br/>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'fields', 'error')} ">
            <label><g:message code="report.fields.label" default="Fields"/></label>

            <g:each in="${reportInstance?.fields?.sort { it.order }}" var="f">
                <span class="property-value" aria-labelledby="fields-label">
                    <g:link controller="fieldReport" action="show" id="${f.id}">
                        ${f?.label} |
                    </g:link>
                </span>
            </g:each>
            <br/>

            <g:link class="btn btn-default" controller="fieldReport" action="create" params="['report.id': reportInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'fieldReport.label', default: 'FieldReport')])}
            </g:link>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="fieldcontain ${hasErrors(bean: reportInstance, field: 'parameters', 'error')} ">
            <label><g:message code="report.parameters.label" default="Parameters"/></label>

            <g:each in="${reportInstance?.parameters?.sort { it.order }}" var="p">
                <span class="property-value" aria-labelledby="parameters-label">
                    <g:link controller="parameterReport" action="show" id="${p.id}">
                        ${p?.label} |
                    </g:link>
                </span>
            </g:each>
            <br/>

            <g:link class="btn btn-default" controller="parameterReport" action="create" params="['report.id': reportInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'parameterReport.label', default: 'ParameterReport')])}
            </g:link>
        </div>
    </div>
</div>



