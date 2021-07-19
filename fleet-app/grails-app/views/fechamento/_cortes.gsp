<%@ page import="com.sysdata.gestaofrota.StatusCorte" %>

<div class="panel panel-default">
    <div class="panel-heading">
        Cortes por Cliente
    </div>
    <div class="panel-body">
        <g:if test="${cortes}">
            <table class="table table-striped table-hover table-condensed">
                <thead>
                <tr>
                    <th>Data Prevista</th>
                    <th>Data Ocorrência</th>
                    <th>Data Vencimento</th>
                    <th>Status</th>
                    <th>Liberado</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${cortes}" var="corte">
                    <tr>
                        <td><a href="#" onclick="abrirFatura(${corte.id})">${corte.dataPrevista.format('dd/MM/yyyy')}</a></td>
                        <td>${corte.dataFechamento?.format('dd/MM/yyyy')}</td>
                        <td>${corte.dataCobranca?.format('dd/MM/yyyy')}</td>
                        <td>${corte.status.nome}</td>
                        <td>${corte.liberado?'Sim':'Não'}</td>

                        <g:if test="${corte.status == StatusCorte.FECHADO}">
                            %{--<td><g:link action="downloadBoleto" params="[corId:corte.id,prgId:prgId]">Boleto</g:link></td>--}%
                        <td><a id="undoCorte" href="#" data-corte="${corte.id}"><i class="glyphicon glyphicon-step-backward"></i></a></td>
                         </g:if>
                        <g:if test="${!corte.liberado}">
                        <td><a id="releaseCorte" href="#" data-corte="${corte.id}"><i class="glyphicon glyphicon-ok-sign"></i></a></td>
                        </g:if>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="well text-center">Sem Faturamentos até o momento</div>
        </g:else>
    </div>
    <div class="panel-footer">
        <a type="button" class="btn btn-sm btn-default" title="Voltar" onclick="abrirFechamentos(${prgId})">
            <i class="glyphicon glyphicon-arrow-left"></i> Voltar
        </a>
    </div>
</div>

<script>
    $("a#undoCorte").click(function() {
        var corteId = $(this).data("corte");

        showModal($("#modal"), 'question', 'O usuário confirma o desfaturamento do Corte selecionado?',
                function() {
                    $.post("${createLink(controller: 'fechamento', action: 'undoCorte')}", {id: corteId})
                        .done(function(resp) {
                            atualizarTabelaFechamentos();
                            alert(resp.msg);
                        })
                        .fail(function(xhr) {
                            console.log(xhr.responseText);
                            alert(xhr.responseText);
                        })
                }
        );
    })

    $("a#releaseCorte").click(function() {
        var corteId = $(this).data('corte');
        $.post("${createLink(controller: 'fechamento', action: 'releaseCorte')}", {id: corteId})
            .done(function(data) {
                $("#fechamento-index").html(data);
            })
            .fail(function(error) {
                console.error(error.responseText);
                alert(error.responseText);
            })
    });
</script>
