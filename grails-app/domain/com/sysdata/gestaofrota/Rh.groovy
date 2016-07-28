package com.sysdata.gestaofrota

class Rh extends Empresa {
	
	String codigo
	Double taxaPedido=0
	Integer validadeCarga
	Integer maximoTrnPorDia
	Integer diasInatividade
	BigDecimal taxaUtilizacao=0
	BigDecimal taxaMensalidade=0
	BigDecimal taxaEmissaoCartao=0
	BigDecimal taxaReemissaoCartao=0
	
	static hasMany=[unidades:Unidade,categoriasFuncionario:CategoriaFuncionario,empresas:PostoCombustivel,role:Role]
	
    static constraints = {
		taxaUtilizacao nullable: true
		taxaMensalidade nullable: true
		taxaEmissaoCartao nullable: true
		taxaReemissaoCartao nullable: true
    }

    String toString() {
        def flat = ""
        this.properties.each {
            flat += "${it}\n"
        }
        flat
    }

}
