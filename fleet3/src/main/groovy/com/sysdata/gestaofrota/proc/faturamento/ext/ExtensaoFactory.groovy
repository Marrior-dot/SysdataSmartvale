package com.sysdata.gestaofrota.proc.faturamento.ext

/**
 * Created by acception on 22/03/18.
 */
class ExtensaoFactory {

    private static def instances=[:]

    static ExtensaoFaturamento getInstance(Class clazz){
        if(!instances.containsValue(clazz)){
            instances[clazz]=clazz.newInstance()
        }
        instances[clazz]

    }

}
