package com.sysdata.gestaofrota

class Boleto {

    Date dateCreated
    String titulo
    String linhaDigitavel
    String nossoNumero
    int via = 1
    Date dataVencimento
    StatusBoleto status = StatusBoleto.CRIADO
    BigDecimal valor
    Arquivo arquivo

    static belongsTo = [fatura: Fatura]

    static constraints = {
        titulo nullable: true
        linhaDigitavel nullable: true
        nossoNumero nullable: true
        arquivo nullable: true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "boleto_seq"]
    }
}
