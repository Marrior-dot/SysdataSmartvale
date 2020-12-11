package com.sysdata.gestaofrota

import spock.lang.Specification

class TDESChipherSpec extends Specification {


    void "testar senha criptografada"() {

        when:

            TDESChipher tdesChipher = new TDESChipher("CBC", "9DDCF7CDC1B96725835D58AB404C62D0")

            println "Original: 7641"
            def hexCipher = tdesChipher.encrypt("7641")

            println "Cifrado: $hexCipher"

            def plainText = tdesChipher.decrypt(hexCipher)
            println "Decifrado: $plainText"

        then:
            plainText == "7641"


    }
}