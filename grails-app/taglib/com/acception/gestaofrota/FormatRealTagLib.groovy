package com.acception.gestaofrota

import com.sysdata.gestaofrota.Util

class FormatRealTagLib {

    Closure formatReal = { Map attrs ->
        def value = attrs.value ?: 0.0D
        out << "R\$ ${Util.formatCurrency(value)}"
    }

}
