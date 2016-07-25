import com.sysdata.gestaofrota.MarcaVeiculo

fixture{
    println "CRIANDO MARCAS DE CARRO..."
    gm(MarcaVeiculo,nome:"GM")
    fiat(MarcaVeiculo,nome:"FIAT")
    volks(MarcaVeiculo,nome:"VOLKSWAGEM")
    ford(MarcaVeiculo,nome:"FORD")
    honda(MarcaVeiculo,nome:"HONDA")
    toyota(MarcaVeiculo,nome:"TOYOTA")
}