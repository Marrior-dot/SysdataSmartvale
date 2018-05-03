package com.sysdata.gestaofrota

class Boleto {

    String titulo
    String codigoBarras
    int via = 1
    Date dateCreated
    StatusBoleto status = StatusBoleto.CRIADO


    static belongsTo = [fatura:Fatura]

    static constraints = {
    }
}
