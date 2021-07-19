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

    static transients = ['hodometroAtualizado', 'identificacaoCompacta']

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

    String getIdentificacaoCompacta() {
        return "(${this.placa}) ${this.marca.nome} ${this.modelo}"
    }

    Long getHodometroAtualizado() {

        def hodometroAntigo = this.hodometro?:0
        if (!this.hodometro ) {
            def ultTr = Transacao.executeQuery("""
select max(t)
from Transacao t
where t.maquina.id =:maq and
t.statusControle in (:sts) and
t.tipo = :tipo""",
                                                [   maq: this.id,
                                                    sts: [
                                                             StatusControleAutorizacao.PENDENTE,
                                                             StatusControleAutorizacao.CONFIRMADA
                                                         ],
                                                    tipo: TipoTransacao.COMBUSTIVEL
                                                ])

            if (ultTr[0]){
                if (ultTr[0].quilometragem == null)
                    throw new RuntimeException("VEI #${this.placa} - Hodômetro não informado na última transação válida!")

                this.hodometro = ultTr[0].quilometragem
                HistoricoHodometro historicoHodometro = new HistoricoHodometro(veiculo: this,
                                                                hodometroAntigo: hodometroAntigo,
                                                                hodometroNovo: this.hodometro)


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