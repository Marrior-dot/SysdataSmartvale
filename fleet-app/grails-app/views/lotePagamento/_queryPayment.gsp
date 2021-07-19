
<div class="panel panel-default">
    <div class="panel-body">
        <g:if test="${response}">
            <g:if test="${response.statusCode == 200}">
                <table class="table">
                    <tbody>
                        <tr>
                            <th>NSL</th>
                            <td>${response.json.nsl}</td>
                        </tr>
                        <tr>
                            <th>Data Contábil</th>
                            <td>${response.json.dataContabil}</td>
                        </tr>
                        <g:if test="${response.json.ted}">
                        <tr>
                            <th>NSR</th>
                            <td>${response.json.ted[0].nsr}</td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td>${response.json.ted[0].status}</td>
                        </tr>
                        <tr>
                            <th>Hora Processamento</th>
                            <td>${response.json.ted[0].horaProcessamento}</td>
                        </tr>
                        </g:if>
                        <g:if test="${response.json.tef}">
                        <tr>
                            <th>NSR</th>
                            <td>${response.json.tef[0].nsr}</td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td>${response.json.tef[0].status}</td>
                        </tr>
                        <tr>
                            <th>Mensagem</th>
                            <td>${response.json.tef[0].mensagem}</td>
                        </tr>
                        <tr>
                            <th>Hora Processamento</th>
                            <td>${response.json.tef[0].horaProcessamento}</td>
                        </tr>
                        </g:if>
                    </tbody>
                </table>
            </g:if>
            <g:else>
                <g:if test="${response.json}">
                    <table class="table">
                        <tr>
                            <th>Tipo de Erro</th>
                            <td>${response.json.tipoErro}</td>
                        </tr>
                        <tr>
                            <th>Código de Erro</th>
                            <td>${response.json.codErro}</td>
                        </tr>
                        <tr>
                            <th>Mensagem de Erro</th>
                            <td>${response.json.msgErro}</td>
                        </tr>
                    </table>
                </g:if>
                <g:else>
                    <div class="alert alert-danger" role="alert">
                        <p><span class="glyphicon glyphicon-exclamation-sign"></span> HTTP Status: ${response.statusCode}</p>
                        <p><span class="glyphicon glyphicon-exclamation-sign"></span> Erro de servidor não identificado</p>
                    </div>
                </g:else>
            </g:else>
        </g:if>
        <g:if test="${error}">
            <div class="alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign"></span>
                ${error}
            </div>
        </g:if>
    </div>
</div>




