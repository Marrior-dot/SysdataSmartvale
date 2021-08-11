import com.sysdata.gestaofrota.StatusTransacao
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import grails.util.Holders


CorretorAgendaDataRepasse corretorAgenda = new CorretorAgendaDataRepasse(ctx.getBean('reembolsoService'))
corretorAgenda.execute()


class CorretorAgendaDataRepasse {

    private def sessionFactory = Holders.grailsApplication.mainContext.sessionFactory

    private def reembolsoService

    def logFile = new File("/home/luiz/tmp/frota/smartvale/correcao_agenda_ecs.log")

    CorretorAgendaDataRepasse(reembolsoService) {
        this.reembolsoService = reembolsoService
    }


    def execute() {
/*
        def transacoesList = Transacao.withCriteria {
            projections {
                property "id"
            }
            'in'("tipo", [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS])
            eq("status", StatusTransacao.AGENDADA)
            order('dateCreated')
        }
*/

        def transacoesList = [1151]

        transacoesList.eachWithIndex { tid, idx ->
            Transacao transacao = Transacao.get(tid)
            def dataRepasse = reembolsoService.calcularDataReembolso(transacao.estabelecimento.empresa, transacao.dataHora)
            println "Data Calc: ${dataRepasse.format('dd/MM/yy')}"
            def repasseLancamento = transacao.lancamentos.find { it.tipo == TipoLancamento.REEMBOLSO }
            if (repasseLancamento) {
                println "LC #${repasseLancamento.id} ${repasseLancamento.dataEfetivacao.format('dd/MM/yy')}"
                if (repasseLancamento.dataEfetivacao != dataRepasse) {
                    log("TR #${transacao.id} => EC: #${transacao.estabelecimento.empresa.id} | Dat: ${transacao.dataHora.format('dd/MM/yy')} | Repasse (${repasseLancamento.dataEfetivacao.format('dd/MM/yy')} -> ${dataRepasse.format('dd/MM/yy')})")
                    repasseLancamento.dataEfetivacao = dataRepasse
                    repasseLancamento.save()
                }
            } else
                throw new RuntimeException("TR #${transacao.id} => Repasse não encontrado!")

            if ((idx + 1) % 50 == 0) {
                sessionFactory.currentSession.flush()
                sessionFactory.currentSession.clear()
            }
        }

    }

    def log(message) {
        println message
        logFile.append(message + "\n")
    }
}





