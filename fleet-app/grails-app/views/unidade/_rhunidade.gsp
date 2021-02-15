<div class="well well-lg panel-top">
    <div class="row">
        <div class="col-md-3">
            <h4><strong>Empresa</strong></h4>
            <h5><g:link controller="rh" action="show" id="${instance?.unidade?.rh?.id}">${instance?.unidade?.rh?.nome}</g:link></h5>
        </div>
        <div class="col-md-3">
            <h4><strong>Unidade</strong></h4>
            <h5><g:link controller="unidade" action="show" id="${instance?.unidade?.id}">${instance?.unidade?.nome}</g:link></h5>
        </div>
    </div>
</div>
