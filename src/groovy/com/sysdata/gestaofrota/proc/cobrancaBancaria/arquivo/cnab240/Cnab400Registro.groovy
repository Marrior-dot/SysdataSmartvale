package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Registro {

    static int sequence = 0

    static def defCampos

    def campos = []

    static int nextSequence() {
        return ++sequence
    }

    private def formatarValor(name,args){
        def fld = defCampos.find { it.id == name }
        if (!fld) throw new RuntimeException("Campo ($name) nao mapeado no registro (${this.class.simpleName})! Verifique configuracao")
        def val
        if (fld.type) {
            switch (fld.type) {
                case DataType.NUMERIC:
                    if (args instanceof Integer || args instanceof Long) val = sprintf("%0${fld.size}d", args)
                    else throw new RuntimeException("Valor ($args) para o campo ($name) invalido!")
                    break
                case DataType.ALPHA:
                    if(args && args.length()>fld.size) args=args[0..fld.size-1]
                    args=Util.normalize(args)
                    args=args.toUpperCase()
                    val = sprintf("%-${fld.size}s", args)
                    break
                case DataType.DATE:
                    val = args.format(fld.format)
                    break
                default:
                    throw new RuntimeException("Tipo de Dado nao identificado! Verifique configuracao")
            }
        } else
            val = args

        [defi: fld, valor: val]
    }

    void setProperty(String name, args) {
        if (name != "defCampos") {
            campos<<formatarValor(name,args)
        } else this.@"$name" = args
    }

    String flatten() {
        StringBuilder sb = new StringBuilder()
        defCampos.each{df->
            def valor
            def campo=this.campos.find{it.defi.id==df.id}
            if(campo) valor=campo.valor
            else {
                def ret
                if (!df.val) throw new RuntimeException("${this.class.simpleName} - Campo ($df.id) sem valor definido!")
                if (df.val instanceof Closure) ret = formatarValor(df.id, df.val())
                else ret = formatarValor(df.id,df.val)
                valor=ret.valor
                this.campos << ret
            }
            sb.append valor
        }
        sb.toString()+"\r\n"
    }
}
