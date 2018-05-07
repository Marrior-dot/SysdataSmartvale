package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Registro {

    private static int sequence = 0

    static def campos

    static int nextSequence() {
        ++sequence
    }

    void setProperty(String name, args) {
        if (name != "campos") {

            def fld = campos.find { it.id == name }
            if (!fld) throw new RuntimeException("Campo ($name) nao mapeado no registro (${this.class.simpleName})! Verifique configuracao")
            if (fld.type) {
                switch (fld.type) {
                    case DataType.NUMERIC:
                        if (args instanceof Integer || args instanceof Long) fld.val = sprintf("%0${fld.size}d", args)
                        else throw new RuntimeException("Valor ($args) para o campo ($name) invalido!")
                        break
                    case DataType.ALPHA:
                        fld.val = sprintf("%-${fld.size}s", args)
                        break
                    case DataType.DATE:
                        fld.val = args.format(fld.format)
                        break

                    default:
                        throw new RuntimeException("Tipo de Dado nao identificado! Verifique configuracao")
                }
            }else
                fld.val=args

        }else this.@"$name"=args
    }

    String flatten() {
        StringBuilder sb = new StringBuilder()
        campos.each {
            sb.append(it.val)
        }
        sb.toString()
    }
}
