<div class="panel panel-default panel-top">
    <div class="panel-heading">
        Agenda de Reembolso
    </div>

    <div class="panel-body">
        <div class="row">
            <div class="col-md-6">
                <table class="table">
                    <thead>
                    <th>Reembolsar em (dias)</th>
                    <th>Ações</th>
                    </thead>
                    <tbody>
                    <g:each in="${empresa.reembolsos}" var="reemb">
                        <tr>
                            <td>${reemb.diasTranscorridos}</td>
                            <td><a href="#" onclick="deleteReembolso(${reemb.id});"><i class="glyphicon glyphicon-trash"></i></a></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>


