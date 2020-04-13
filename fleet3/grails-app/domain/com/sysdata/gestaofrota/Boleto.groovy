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

    static belongsTo = [fatura:Fatura]

    static constraints = {
    }
}
