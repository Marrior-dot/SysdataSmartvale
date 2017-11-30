package com.sysdata.gestaofrota

class Equipamento extends MaquinaMotorizada {
    String codigo
    String descricao
    Long mediaConsumo
    TipoEquipamento tipo

    static constraints = {
        codigo blank: false, unique: true
        descricao blank: false, nullable: true
        mediaConsumo blank: false
        tipo blank: false
    }

    static mapping = {
    }

    @Override
    String getNomeEmbossing() {
        "${codigo} ${tipo.abreviacao} ${complementoEmbossing}".toUpperCase()
    }
}
