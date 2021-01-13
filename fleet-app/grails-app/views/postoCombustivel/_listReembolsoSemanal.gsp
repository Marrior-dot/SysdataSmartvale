<div class="panel panel-default panel-top">
    <div class="panel-heading">
        Agenda de Reembolso Semanal
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-md-6">
                <table class="table">
                    <thead>
                    <th>Dia Semana</th>
                    <th>Intervalo Dias</th>
                    <th>Açoes</th>
                    </thead>
                    <tbody>
                    <g:each in="${empresa.reembolsos}" var="reemb">
                        <tr>
                            <td>${reemb.diaSemana}</td>
                            <td>${reemb.intervaloDias}</td>
                            <td><a href="#" onclick="deleteReembolso(${reemb.id});"><i class="glyphicon glyphicon-trash"></i></a></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>


