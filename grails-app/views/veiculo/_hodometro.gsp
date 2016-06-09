<g:form>
    <div class="panel panel-default">
        <div class="panel-heading"></div>
        <div class="panel-body">
            <div class="buttons">
                <g:actionSubmit class="btn btn-default" action="zerarHodometro" value="Zerar Hodômetro" />
            </div>

            <br>

            <div class="row">

                <div class="col-xs-4">
                    <bs:formField id="hodometro" name="hodometro" label="Última Leitura" value="${veiculoInstance?.hodometro}"></bs:formField>
                </div>
            </div>

        </div>
    </div>

</g:form>


