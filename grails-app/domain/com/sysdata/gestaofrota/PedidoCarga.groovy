package com.sysdata.gestaofrota

import java.math.RoundingMode

class PedidoCarga {
	
	Date dateCreated
	Date dataCarga
	StatusPedidoCarga status=StatusPedidoCarga.NOVO
	Unidade unidade
	Integer validade
	User usuario
	Double taxa=0D
	Double total=0D
	
	static hasMany=[itens:ItemPedido]
	
    static constraints = {
//		dataCarga(validator:{val,obj->
//								def hj=new Date()
//								if(val.getTime()!=hj.getTime() && val?.before(hj))
//									return "pedidoCarga.dataCarga.invalida"
//							})
		
		usuario(nullable:true)
		taxa nullable:true
		total nullable:true
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'pedidocarga_seq']
	}
	
	
	def calcularTotal(){
		this.total=0D
		//Calcular Total pelos itens 
		this.itens.each{item->
			this.total+=item.valor
		}
		
		this.taxa=this.unidade.rh.taxaPedido
		BigDecimal tx=BigDecimal.valueOf(this.taxa?:0D)
		BigDecimal tot=BigDecimal.valueOf(this.total)
		BigDecimal calc=tot*tx/100D
		calc.setScale(2,RoundingMode.HALF_UP)
		this.total+=calc
	}
	
	
}
