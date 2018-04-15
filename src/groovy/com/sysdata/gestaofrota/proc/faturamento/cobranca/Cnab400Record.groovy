package com.sysdata.gestaofrota.proc.faturamento.cobranca

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Record {

    private static int sequence=0

    protected def fields

    int nextSequence(){
        ++sequence
    }

    def setProperty(String name,args){
        def fld=fields.find{it.id==name}
        if(!fld) throw new RuntimeException("Campo ($name) nao mapeado! Verifique configuracao")

        if(fld.type){

            switch(fld.type){
                case DataType.NUMERIC:
                    fld.val=sprintf("%0${fld.size}d",args)
                    break
                case DataType.ALPHA:
                    fld.val=sprintf("%-${fld.size}s",args)
                    break
                case DataType.DATE:
                    fld.val=args.format(fld.format)
                    break

                default:
                    throw new RuntimeException("Tipo de Dado nao identificado! Verifique configuracao")
            }


        }

    }




}
