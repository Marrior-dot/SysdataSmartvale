package com.sysdata.gestaofrota

class Boleto {

    String titulo
    String linhaDigitavel
    String nossoNumero
    int via = 1
    Date dateCreated
    Date dataVencimento
    StatusBoleto status = StatusBoleto.CRIADO
    byte[] imagem
    BigDecimal valor

    static belongsTo = [fatura: Fatura]

    static constraints = {
        titulo nullable: true
        linhaDigitavel nullable: true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "boleto_seq"]
    }
}
