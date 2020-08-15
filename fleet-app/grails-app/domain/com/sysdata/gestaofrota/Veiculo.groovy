package com.sysdata.gestaofrota


class Veiculo extends MaquinaMotorizada {
    String placa
    MarcaVeiculo marca
    String modelo
    String chassi
    String anoFabricacao
    Long autonomia
    Date validadeExtintor
    Long hodometro = 0

    static transients = ['hodometroAtualizado']

    static constraints = {
        validadeExtintor(nullable: true)
        placa unique: true, blank: false
        chassi unique: true, blank: false
        marca blank: false
        autonomia blank: false
        modelo blank: false
        autonomia nullable: true
    }

    static mapping = {
    }

    static hibernateFilters = {
        veiculosPorRh(condition: 'unidade_id in (select u.id from Unidade u where u.rh_id = :rh_id)', types: 'long')
    }


    Long getHodometroAtualizado() {

        def hodometroAntigo = this.hodometro?:0
        if (!this.hodometro ) {
            def ultTr = Transacao.executeQuery("select max(t) from Transacao t where t.maquina.id =:maq and t.statusControle in (:sts)",
                                                [maq: this.id,
                                                 sts: [StatusControleAutorizacao.PENDENTE, StatusControleAutorizacao.CONFIRMADA]])

            if (ultTr[0]){
                this.hodometro = ultTr[0].quilometragem
                HistoricoHodometro historicoHodometro = new HistoricoHodometro(veiculo: this,hodometroAntigo: hodometroAntigo, hodometroNovo: this.hodometro)
                historicoHodometro.save()
                this.save()
            }
            else {
                this.hodometro = 0
            }
        }

        return this.hodometro
    }

    @Override
    String getNomeEmbossing() {
        "${placa} ${marca?.abreviacao} ${complementoEmbossing}".toUpperCase()
    }
}