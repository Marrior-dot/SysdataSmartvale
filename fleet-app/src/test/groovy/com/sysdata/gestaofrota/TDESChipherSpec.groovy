package com.sysdata.gestaofrota

import spock.lang.Specification

class TDESChipherSpec extends Specification {


    void "testar senha criptografada"() {

        when:

            TDESChipher tdesChipher = new TDESChipher("CBC", "A7DAA1324C623EF2CB70704CC4D3F249")

            println "Original: 6878"
            def hexCipher = tdesChipher.encrypt("6878")

            println "Cifrado: $hexCipher"

            def plainText = tdesChipher.decrypt(hexCipher)
            println "Decifrado: $plainText"

        then:
            plainText == "6878"


    }
}