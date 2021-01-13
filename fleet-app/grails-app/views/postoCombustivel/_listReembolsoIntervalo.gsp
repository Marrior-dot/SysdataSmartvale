<div class="panel panel-default panel-top">
    <div class="panel-heading">
        Agenda de Reembolso por Intervalos
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-md-6">
                <table class="table">
                    <thead>
                    <th>Dia Inicial</th>
                    <th>Dia Final</th>
                    <th>Dia Efetivação</th>
                    <th>Meses</th>
                    <th>Açoes</th>
                    </thead>
                    <tbody>
                    <g:each in="${empresa.reembolsos.sort{ it.inicioIntervalo }}" var="reemb">
                        <tr>
                            <td>${reemb.inicioIntervalo}</td>
                            <td>${reemb.fimIntervalo}</td>
                            <td>${reemb.diaEfetivacao}</td>
                            <td>${reemb.meses}</td>
                            <td><a href="#" onclick="deleteReembolso(${reemb.id});"><i class="glyphicon glyphicon-trash"></i></a></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>


