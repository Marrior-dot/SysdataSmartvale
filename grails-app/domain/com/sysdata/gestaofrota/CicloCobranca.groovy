package com.sysdata.gestaofrota

class CicloCobranca {

    Date dateCreated
    String referencia
    StatusCiclo status

    static transients = ['proximaReferencia']

    static constraints = {
    }

    String getProximaReferencia(){

        def ano=this.referencia[0..3] as int
        def mes=this.referencia[4..5] as int

        if(mes+1>12){
            mes=1
            ano++
        }else mes++


        sprintf("%04d%02d",ano,mes)
    }

    private static CicloCobranca abrirNovoCiclo(ref){
        def cicloAberto=new CicloCobranca(referencia:ref,status:StatusCiclo.ABERTO)
        cicloAberto.save(flush:true)
        cicloAberto
    }

    static CicloCobranca getCicloAberto(){

        def hj=new Date()
        def mes=hj.getAt(Calendar.MONTH)+1
        def ano=hj.getAt(Calendar.YEAR)
        def validRef=sprintf("%04d%02d",ano,mes)

        def cicloAberto

        def ultCiclo=CicloCobranca.withCriteria(uniqueResult:true) {
                        maxResults(1)
                        order("id","desc")
                    }
        if(ultCiclo){
            if(ultCiclo.status==StatusCiclo.ABERTO){
                if(ultCiclo.referencia!=validRef){

                    //Fecha ciclo
                    ultCiclo.status=StatusCiclo.FECHADO
                    ultCiclo.save(flush:true)

                    //Abre novo ciclo
                    cicloAberto=abrirNovoCiclo(validRef)


                }else cicloAberto=ultCiclo
            }else if(ultCiclo.status==StatusCiclo.FECHADO)
                cicloAberto=abrirNovoCiclo(validRef)

        }else
            cicloAberto=abrirNovoCiclo(validRef)


        cicloAberto
    }



}
