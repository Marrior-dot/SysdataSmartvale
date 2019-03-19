package com.sysdata.gestaofrota


class Veiculo extends MaquinaMotorizada {
    String placa
    MarcaVeiculo marca
    String modelo
    String chassi
    String anoFabricacao
    Long autonomia
    Date validadeExtintor
    Long hodometro

    static constraints = {
        validadeExtintor(nullable: true)
        placa unique: true, blank: false
        chassi unique: true, blank: false
        marca blank: false
        autonomia blank: false
        modelo blank: false
    }

    static mapping = {
    }
    @Override
    Long getHodometro() {
        def hodometroAntigo = this.hodometro?:0
        if (!this.hodometro) {
            def ultTr = Transacao
                    .executeQuery("select max(t) from Transacao t where t.maquina=:maq and t.statusControle in (:sts)",
                    [maq: this, sts: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]])
            if (ultTr[0]){
                this.hodometro = ultTr[0].quilometragem
                HistoricoHodometro historicoHodometro = new HistoricoHodometro(veiculo: this,hodometroAntigo: hodometroAntigo, hodometroNovo: this.hodometro)
                historicoHodometro.save()
                println "Veiculo: ${this.placa} - Hodometro antigo: ${hodometroAntigo} - Hodometro Novo: ${this.hodometro}"
            }

            else this.hodometro = 0
        }
        return this.hodometro
    }

    @Override
    String getNomeEmbossing() {
        "${placa} ${marca?.abreviacao} ${complementoEmbossing}".toUpperCase()
    }
}