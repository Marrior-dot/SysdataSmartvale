package com.sysdata.gestaofrota

import spock.lang.Specification

class TDESChipherSpec extends Specification {


    void "testar senha criptografada"() {

        when:

            TDESChipher tdesChipher = new TDESChipher("CBC", "C2385BF4F73B7029D6A410529101D3C2")

/*
            println "Original: 7641"
            def hexCipher = tdesChipher.encrypt("7641")
*/

            //println "Original: 7641"
            def hexCipher = "5fc6a87fbdc21f73"

            println "Cifrado: $hexCipher"

            def plainText = tdesChipher.decrypt(hexCipher)
            println "Decifrado: $plainText"

        then:
            plainText == "7141"


    }
}