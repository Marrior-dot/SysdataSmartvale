<%@ page import="br.com.acception.greport.MarkupType" %>

<g:if test="${reportInstance?.parameters}">

	<fieldset>
		<g:each in="${reportInstance.parameters.sort{it.order}}" var="parameterInstance">
			<div>
				<label>
					<span>${parameterInstance.label}</span>
					<g:if test="${parameterInstance.markupType==MarkupType.TEXT}">
						<g:textField name="${parameterInstance.name}" value='${params[("${parameterInstance.name}")]}'/>
					</g:if>
					<g:elseif test="${parameterInstance.markupType==MarkupType.DATE_PICKER}">
						<%
							dateStr=params[("${parameterInstance.name}")]
							def dateVal
							if(dateStr)
								dateVal=new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dateStr)
					 	%>
						<gui:datePicker id="${parameterInstance.name}" name="${parameterInstance.name}"  formatString="dd/MM/yyyy"
						value="${dateVal}"/>						
					</g:elseif>
				</label>
			</div>
		</g:each>
	</fieldset>
</g:if>
