<%@ page import="com.sysdata.gestaofrota.Util; java.text.SimpleDateFormat; br.com.acception.greport.MarkupType" %>

<g:if test="${reportInstance?.parameters}">
    <g:set var="reportParameters" value="${reportInstance.parameters.sort { it.order }}"/>

    <g:if test="${reportParameters.size() <= 4}">
        <div class="row">
            <g:each in="${reportParameters}" var="parameterInstance">
                <g:render template="/reportViewer/parameter" model="${[parameterInstance: parameterInstance]}"/>
            </g:each>
        </div>
    </g:if>
    <g:elseif test="${reportParameters.size() <= 8}">
        <div class="row">
            <g:each in="${reportParameters[0..3]}" var="parameterInstance">
                <g:render template="/reportViewer/parameter" model="${[parameterInstance: parameterInstance]}"/>
            </g:each>
        </div>
        <div class="row">
            <g:each in="${reportParameters[4..(reportParameters.size()-1)]}" var="parameterInstance">
                <g:render template="/reportViewer/parameter" model="${[parameterInstance: parameterInstance]}"/>
            </g:each>
        </div>
    </g:elseif>
</g:if>
