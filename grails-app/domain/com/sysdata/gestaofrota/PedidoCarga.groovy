package com.sysdata.gestaofrota

class PedidoCarga {
	
	Date dateCreated
	Date dataCarga
	StatusPedidoCarga status=StatusPedidoCarga.NOVO
	Unidade unidade
	Integer validade
	User usuario
	
	static hasMany=[itens:ItemPedido]
	

    static constraints = {
//		dataCarga(validator:{val,obj->
//								def hj=new Date()
//								if(val.getTime()!=hj.getTime() && val?.before(hj))
//									return "pedidoCarga.dataCarga.invalida"
//							})
		
		usuario(nullable:true)
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'pedidocarga_seq']
	}
}
