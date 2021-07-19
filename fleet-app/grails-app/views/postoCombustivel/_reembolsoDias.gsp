<div class="panel panel-default">

    <div class="panel-heading">Reembolso Dias Fixos</div>

    <div class="panel-body">

        <div class="error"></div>

        <g:form name="reembDiasForm">
            <g:hiddenField name="id" value="${reembolso?.id}"/>
            <g:hiddenField name="parId" value="${reembolso?.parId}"/>

            <div class="row">
                <div class="col-md-4 form-group">
                    <label for="diasTranscorridos">Reembolsar em (dias)</label>
                    <g:field type="number" name="diasTranscorridos" class="form-control required" value="${reembolso?.diasTranscorridos}"></g:field>
                </div>
            </div>
        </g:form>
    </div>

</div>