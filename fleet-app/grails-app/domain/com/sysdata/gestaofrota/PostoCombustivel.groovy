package com.sysdata.gestaofrota

class PostoCombustivel extends Empresa {

    BigDecimal taxaReembolso = 0.0
    TipoReembolso tipoReembolso

    BigDecimal taxaAdesao = 0.0
    BigDecimal taxaVisibilidade = 0.0
    BigDecimal anuidade = 0.0
    BigDecimal tarifaBancaria = 0.0

    static belongsTo = Rh

    static hasMany = [estabelecimentos: Estabelecimento, reembolsos: Reembolso, programas: Rh]
    //, role: Role

    static constraints = {
        tipoReembolso(nullable: true)
        taxaAdesao nullable: true
        taxaVisibilidade nullable: true
        anuidade nullable: true
        tarifaBancaria nullable: true
    }

    def vinculado(Rh programa) {
        return this.programas && this.programas.contains(programa)
    }
}
