package com.sysdata.gestaofrota

class PostoCombustivel extends Empresa {
    BigDecimal taxaReembolso = 0.0
    TipoReembolso tipoReembolso

    static belongsTo = Rh

    static hasMany = [estabelecimentos: Estabelecimento, reembolsos: Reembolso, programas: Rh, role: Role]

    static constraints = {
        tipoReembolso(nullable: true)
    }

    def vinculado(Rh programa) {
        return this.programas && this.programas.contains(programa)
    }
}
