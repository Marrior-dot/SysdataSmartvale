<div class="panel panel-default">

    <div class="panel-heading">Reembolso Dias Transcorridos</div>

    <div class="panel-body">
        <div class="row">
            <div class="col-md-4 form-group">
                <label>Reembolsar em (dias):</label>

                <g:form id="reembDiasForm">
                    <g:hiddenField name="id" value="${reembolso?.id}"/>
                    <g:hiddenField name="participante.id" value="${reembolso?.participante?.id}"/>

                    <g:field type="number" name="diasTranscorridos" class="form-control required" value="${reembolso?.diasTranscorridos}"></g:field>
                </g:form>

            </div>
        </div>
    </div>

</div>