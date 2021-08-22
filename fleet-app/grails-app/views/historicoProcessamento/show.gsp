<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <title>Detalhe Histórico de Processamento</title>

    <style>
        table  {
            table-layout: fixed;
            word-wrap: break-word;
        }
        table th {
            width: 20%;
        }
    </style>


</head>
<body>

<div class="panel panel-default panel-top">

    <div class="panel-heading">
        <h4>Detalhe Histórico de Processamento</h4>
    </div>

    <div class="panel-body">
        <table class="table table-bordered">
            <tr>
                <th>Processamento</th>
                <td>${historicoProcessamento.processing.name}</td>
            </tr>
            <tr>
                <th>Data/Hora Início</th>
                <td><g:formatDate date="${historicoProcessamento.startTime}" format="dd/MM/yy HH:mm:ss"></g:formatDate></td>
            </tr>
            <tr>
                <th>Data/Hora Fim</th>
                <td><g:formatDate date="${historicoProcessamento.endTime}" format="dd/MM/yy HH:mm:ss"></g:formatDate></td>
            </tr>
            <tr>
                <th>Status</th>
                <td>${historicoProcessamento.executionStatus.name}</td>
            </tr>
            <tr>
                <th>Detalhes</th>
                <td>${historicoProcessamento.details}</td>
            </tr>

        </table>
    </div>
</div>
</body>
</html>