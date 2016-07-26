<%@ page import="com.sysdata.gestaofrota.Util; br.com.acception.greport.MarkupType" %>
<div class="form-group col-md-3">
    <g:if test="${parameterInstance?.markupType == MarkupType.TEXT}">
        <label for="${parameterInstance.name}">${parameterInstance.label}</label>
        <input type="text" class="form-control" name="${parameterInstance.name}"
               id="${parameterInstance.name}"
               value='${params[("${parameterInstance.name}")]}'>
    </g:if>
    <g:elseif test="${parameterInstance?.markupType == MarkupType.DATE_PICKER}">
        <label for="${parameterInstance.name}">${parameterInstance.label}</label>
        <input type="text" id="${parameterInstance.name}" name="${parameterInstance.name}"
               class="form-control datepicker"
               value="${Util.parseDate(params[("${parameterInstance.name}")]) ?: ''}">
    </g:elseif>
    <g:elseif test="${parameterInstance?.markupType == MarkupType.READONLY && params[("${parameterInstance.name}")]}">
        <label for="${parameterInstance.name}">${parameterInstance.label}</label>
        <input class="form-control" type="text" id="${parameterInstance.name}" name="${parameterInstance.name}"
               value='${params[("${parameterInstance.name}")]}' readonly>
    </g:elseif>
</div>