package com.sysdata.gestaofrota

class Rh extends Empresa {

    String codigo
    Double taxaPedido = 0.0
    Integer validadeCarga = 0

    static hasMany = [unidades: Unidade, categoriasFuncionario: CategoriaFuncionario, empresas: PostoCombustivel, role: Role]

    static constraints = {
        taxaPedido nullable: true, blank: true
        validadeCarga nullable: true, blank: true
    }

    String toString() {
        def flat = ""
        this.properties.each {
            flat += "${it}\n"
        }
        flat
    }

}
