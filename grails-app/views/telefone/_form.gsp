<div class="panel panel-default">
    <div class="panel-heading">${legend}</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-xs-2">
                <bs:formField id="${telefone}.ddd" name="${telefone}.ddd" label="DDD"  value="${telefoneInstance?.ddd}" />
            </div>
            <div class="col-xs-2">
                <bs:formField id="${telefone}.numero" name="${telefone}.numero" label="Número"  value="${telefoneInstance?.numero}" />
            </div>
            <div class="col-xs-2">
                <bs:formField id="${telefone}.ramal" name="${telefone}.ramal" label="Ramal"  value="${telefoneInstance?.ramal}" />
            </div>
        </div>
    </div>
</div>



