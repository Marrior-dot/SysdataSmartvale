<div class="panel panel-default">
    <div class="panel-heading">${legend}</div>
    <div class="panel-body">
        <div class="row">

            <div class="col-xs-2">
                <bs:formField id="${telefone}.ddd" class="ddd" name="${telefone}.ddd" label="DDD" value="${telefoneInstance?.ddd}" required="true"/>
            </div>
            <div class="col-xs-4">
                <bs:formField id="${telefone}.numero" class="${className ?: 'home-phone'}" name="${telefone}.numero" label="Número" value="${telefoneInstance?.numero}" required="true"/>
            </div>
            <div class="col-xs-2">
                <bs:formField id="${telefone}.ramal" name="${telefone}.ramal" label="Ramal"  value="${telefoneInstance?.ramal}" />
            </div>
        </div>
    </div>
</div>



