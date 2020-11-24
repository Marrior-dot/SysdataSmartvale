
    <div class="row">
        <div class="col-md-3">
            <label for="dia">Dia p/ Carga</label>
            <g:field type="number" class="form-control" name="agendas[$i].dia" min="1" max="31"
                     value="${pedidoCargaInstance?.agendas ? pedidoCargaInstance.agendas[i].dia : null }">

            </g:field>
        </div>

        <div class="col-md-3">
            <label for="finalizaEm">Finaliza Em</label>
            <g:textField name="agendas[$i].finalizaEm" class="form-control mesAno"
                         value="${pedidoCargaInstance?.agendas ? pedidoCargaInstance.agendas[i].finalizaEm : null}"></g:textField>
        </div>

        <g:if test="${i == (totalAgendas - 1)}">
            <div class="col-md-1" style="margin-top: 20px;">
                <button id="btnAdd" type="button" class="btn btn-success btn-circle"><i class="fa fa-plus"></i></button>
            </div>
        </g:if>
        <g:else>
            <div class="col-md-1" style="margin-top: 20px;">
                <button id="btnDel" type="button" class="btn btn-danger btn-circle"><i class="fa fa-remove"></i></button>
            </div>
        </g:else>


    </div>







