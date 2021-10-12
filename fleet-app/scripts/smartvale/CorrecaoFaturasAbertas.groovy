import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.StatusFatura
import com.sysdata.gestaofrota.TipoFatura


CorretorFaturasAbertas corretorFaturasAbertas = new CorretorFaturasAbertas()
corretorFaturasAbertas.execute()

class CorretorFaturasAbertas {

    private def logFile = new File("/home/luiz/tmp/frota/smartvale/correcao_faturas_abertas.log")

    private def fixFatura(fatAntId) {
        Fatura faturaAnterior = Fatura.get(fatAntId)
        if (faturaAnterior.status != StatusFatura.ABERTA) {
            faturaAnterior.status = StatusFatura.ABERTA
            faturaAnterior.save(flush: true)
            log("CNT #${faturaAnterior.conta.id} FAT #${faturaAnterior.id} => ABERTA")
        }
    }

    def execute() {
        def fatIds = Fatura.executeQuery("select id from Fatura f where f.tipo =:tipo order by f.conta.id, f.dataVencimento",
                                    [tipo: TipoFatura.PORTADOR_POSPAGO])

        if (fatIds) {
            def currContaId
            def fatAntId
            log("Total Faturas: ${fatIds.size()}")
            fatIds.each { fid ->
                Fatura fatura = Fatura.get(fid)
                log("CNT #${fatura.conta.id} FAT #${fatura.id} vcto: ${fatura.dataVencimento.format('dd/MM/yy')}")
                if (currContaId && currContaId != fatura.conta.id)
                    fixFatura(fatAntId)

                fatura.status = StatusFatura.FECHADA
                fatura.save(flush: true)
                currContaId = fatura.conta.id
                fatAntId = fatura.id
            }
            fixFatura(fatAntId)
        }
    }

    def log(message) {
        println message
        logFile.append(message + "\n")
    }

}


