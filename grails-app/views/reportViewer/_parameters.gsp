<%@ page import="com.sysdata.gestaofrota.Util; java.text.SimpleDateFormat; br.com.acception.greport.MarkupType" %>

<g:if test="${reportInstance?.parameters}">

    %{--<g:set var="reportParameters" value="${reportInstance.parameters.sort { it.order }}"/>--}%
    %{--<g:set var="mod" value="${reportParameters?.size() % 4}"/>--}%

    %{--<g:each in="${[0..mod]}" status="i">--}%
        %{--<div class="row">--}%
            %{--<g:each in="${reportParameters[(i * 4).. reportParameters.size() ??? 4]}" var="parameterInstance">--}%
                %{--<g:render template="/reportViewer/parameter" model="${[parameterInstance: parameterInstance]}"/>--}%
            %{--</g:each>--}%
        %{--</div>--}%
    %{--</g:each>--}%


    <div class="row">
        <g:each in="${reportInstance.parameters.sort { it.order }}" var="parameterInstance">
            <g:render template="/reportViewer/parameter" model="${[parameterInstance: parameterInstance]}"/>
        </g:each>
    </div>

</g:if>
