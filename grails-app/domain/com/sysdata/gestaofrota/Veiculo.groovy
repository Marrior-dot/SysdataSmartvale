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

    String getHodometro() {
        if (!this.hodometro) {
            def ultTr = Transacao
                    .executeQuery("select max(t) from Transacao t where t.maquina=:maq and t.statusControle in (:sts)",
                    [maq: this, sts: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]])
            if (ultTr[0]) this.hodometro = ultTr[0].quilometragem
            else this.hodometro = 0
        }
        return this.hodometro
    }
}
