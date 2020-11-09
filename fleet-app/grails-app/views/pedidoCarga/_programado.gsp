
<div class="panel panel-default">

    <div class="panel-heading">
        <h3 class="panel-title">Agendamento do Pedido</h3>
    </div>

    <div id="agenda" class="panel-body">

        <div class="row">

            <div class="col-md-3">
                <g:radioGroup name="recorrencia" labels="['Mensal', 'Datas']" values="[1, 2]" value="1" >
                    <p>${it.radio} ${it.label}</p>
                </g:radioGroup>
            </div>

        </div>

        <g:if test="${pedidoCargaInstance?.agendas}">

            <g:each in="pedidoCargaInstance.agendas" var="agenda" status="idx">
                <g:render template="agendaTemplate" model="[i: idx]"></g:render>
            </g:each>
        </g:if>

        <g:else>
            <g:render template="agendaTemplate" model="[i: 0]"></g:render>
        </g:else>

    </div>
</div>