<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga; com.sysdata.gestaofrota.Util" %>

<div class="row">

    <div class="col-md-3">
        <label class="control-label" for="dataCarga">Data de Carga</label>
        <input type="text" id="dataCarga" name="dataCarga" class="form-control datepicker"
               value="${Util.formattedDate(pedidoCargaInstance?.dataCarga)}"/>
    </div>


    <g:if test="${action != Util.ACTION_NEW && pedidoCargaInstance}">

        <div class="col-md-3">
            <label class="control-label">Status</label>

            <%
                def labelClass
                switch (pedidoCargaInstance?.status) {
                    case StatusPedidoCarga.NOVO:
                        labelClass = "label-primary"
                        break
                    case StatusPedidoCarga.LIBERADO:
                        labelClass = "label-info"
                        break
                    case StatusPedidoCarga.FINALIZADO:
                        labelClass = "label-success"
                        break
                    case StatusPedidoCarga.CANCELADO:
                        labelClass = "label-danger"
                        break
                    default:
                        labelClass = "label-default"
                        break
                }
            %>

            <h5><span class="label ${labelClass}">${pedidoCargaInstance?.status?.nome}</span></h5>

        </div>
    </g:if>

</div>







