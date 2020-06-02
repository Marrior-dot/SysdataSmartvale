<script type="application/javascript">
    function editar(){
        $("input#hodometro").removeAttr("disabled");
        $("button#editarHodometro").hide()
        $("button#salvarHodometro").show()
    }
</script>

<g:form method="post" action="alterarHodometro" >
    <g:hiddenField name="id" value="${veiculoInstance?.id}" />
    <g:hiddenField name="version" value="${funcionarioInstance?.version}" />
    <g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>
    <g:hiddenField name="action" value="${action}"/>
    <div class="panel panel-default">
        <div class="panel-heading"></div>
        <div class="panel-body">
            %{--NÃO IMPLEMENTADO--}%
            %{--<div class="buttons">--}%
                %{--<g:actionSubmit class="btn btn-default" action="zerarHodometro" value="Zerar Hodômetro" />--}%
            %{--</div>--}%

            <br>

            <div class="row">

                <div class="col-xs-4">
                    <bs:formField id="hodometro" class="hodometro" type="number" name="hodometro" label="Última Leitura" value="${veiculoInstance?.hodometroAtualizado ?: 0}"></bs:formField>
                </div>
            </div>

        </div>
    </div>
    <div class="buttons">
        <g:if test="${action=='visualizando'}">
            <button id="editarHodometro" class="btn btn-default" type="button" onclick="editar()">Editar</button>
            <button id="salvarHodometro" class="btn btn-default" type="submit" style="display: none">Salvar</button>
        </g:if>
    </div>

</g:form>


