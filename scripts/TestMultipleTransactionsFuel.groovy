import com.sysdata.gestaofrota.*

def cartoes=["23011","24011","29011","28011","30011"]

class Const{

    static def veiculos=["NSS-1980","JBB-1098","ARF-1234","JVB-1885","AJN-6123"]
    static def estabs=[1,2]
    static def combs=TipoCombustivel.values()
        
    static def pattern="62340940010000"

}



class TransactionMaker {

    def number
    def service


    def doFuelTransaction(cart){
        Cartao.withTransaction{
            Random rand=new Random()
        
            def numCartao=Const.pattern+cart
        
            def cartaoInstance=Cartao.findByNumero(numCartao)
                      
            
            def commandInstance=[cartao:numCartao,    
                                matricula:cartaoInstance?.funcionario?.matricula,
                                placa:Const.veiculos[rand.nextInt(Const.veiculos.size())], // Recupera uma placa aleatoriamente
                                quilometragem:"1000",
                                estabelecimento:String.format("%015d",Const.estabs[rand.nextInt(Const.estabs.size())]),
                                tipoCombustivel:Const.combs[rand.nextInt(Const.combs.size())],
                                valor:"0,01",
                                senha:"12345678",
                                vencimento:"1020"
                                                           ]
            println "[Thread-$number] - Command montado:$commandInstance"
            
            service.handleFuelTransaction(commandInstance)
            
            if(commandInstance.autorizada)
                println "[Thread-$number] - Transacao AUTORIZADA. Cod Aut:${commandInstance.nsuHost}"
            else
                println "[Thread-$number] - Transacao NEGADA. Cod Resp:${commandInstance.codigoRetorno}"
            
            }
    }

}


/* */
cartoes.eachWithIndex{cart,i->
    def authService=ctx.getBean("authServerService")
    Thread.start{
        new TransactionMaker(number:i,service:authService).doFuelTransaction(cart)
    }
}