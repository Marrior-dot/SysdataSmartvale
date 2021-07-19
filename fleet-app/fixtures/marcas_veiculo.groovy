import com.sysdata.gestaofrota.MarcaVeiculo

fixture {
    println "CRIANDO MARCAS DE CARRO..."
    gm(MarcaVeiculo, nome: "General Motos", abreviacao: "gm")
    fiat(MarcaVeiculo, nome: "FIAT", abreviacao: "fiat")
    volks(MarcaVeiculo, nome: "VOLKSWAGEM", abreviacao: "vw")
    ford(MarcaVeiculo, nome: "FORD", abreviacao: "ford")
    honda(MarcaVeiculo, nome: "HONDA", abreviacao: "honda")
    toyota(MarcaVeiculo, nome: "TOYOTA", abreviacao: "toyota")
}