<%@ page import="com.sysdata.gestaofrota.StatusCorte" %>

<div class="panel panel-default">

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
                    <th></th>
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

                         <g:if test="${corte.status==StatusCorte.FECHADO}">
                            <td><g:link action="downloadBoleto" params="[corId:corte.id,prgId:prgId]">Boleto</g:link></td>
                         </g:if>
                        <g:else>
                            <td>----</td>
                        </g:else>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <div class="well text-center">Sem Faturamentos até o momento</div>
        </g:else>

        <a type="button" class="btn btn-sm btn-default" title="Voltar" onclick="abrirFechamentos(${prgId})">
            Voltar</i>
        </a>
    </div>
</div>