<div class="panel panel-default">
    <div class="panel-heading">${legend}</div>
    <div class="panel-body">
        <div class="row">
            <g:if test="${required}">
                <div class="col-xs-2">
                    <bs:formField id="${telefone}.ddd" name="${telefone}.ddd" label="DDD" required="required" value="${telefoneInstance?.ddd}" />
                </div>
                <div class="col-xs-4">
                    <bs:formField id="${telefone}.numero" name="${telefone}.numero" label="Número" required="required" value="${telefoneInstance?.numero}" />
                </div>
            </g:if>
            <g:else>
                <div class="col-xs-2">
                    <bs:formField id="${telefone}.ddd" name="${telefone}.ddd" label="DDD" value="${telefoneInstance?.ddd}" />
                </div>
                <div class="col-xs-4">
                    <bs:formField id="${telefone}.numero" name="${telefone}.numero" label="Número" value="${telefoneInstance?.numero}" />
                </div>
            </g:else>
            <div class="col-xs-2">
                <bs:formField id="${telefone}.ramal" name="${telefone}.ramal" label="Ramal"  value="${telefoneInstance?.ramal}" />
            </div>
        </div>
    </div>
</div>



