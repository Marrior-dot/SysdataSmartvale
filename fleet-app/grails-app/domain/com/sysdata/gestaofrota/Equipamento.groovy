package com.sysdata.gestaofrota

class Equipamento extends MaquinaMotorizada {
    String codigo
    String descricao
    Long mediaConsumo
    TipoEquipamento tipo

    static constraints = {
        codigo blank: false, unique: true
        descricao blank: true, nullable: true
        mediaConsumo blank: false
        tipo blank: false
    }

    static mapping = {
    }

    static transients = ['identificacaoCompacta']

    @Override
    String getNomeEmbossing() {
        "${codigo} ${tipo.abreviacao} ${complementoEmbossing}".toUpperCase()
    }

    String getIdentificacaoCompacta() {
        return "(${this.codigo}) ${this.tipo.nome}"
    }
}
