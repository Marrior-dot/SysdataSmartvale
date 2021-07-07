package com.sysdata.gestaofrota

class StatusRetornoPagamento {

    String codigo
    String descricao


    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "stsretpgto_seq"]
    }

    static Integer findNextCodigo() {
        def count = StatusRetornoPagamento.count()
        while (StatusRetornoPagamento.findByCodigo(count++)) {}
        return count
    }

    String toString() {
        return "${this.codigo} - ${this.descricao}"
    }

}
