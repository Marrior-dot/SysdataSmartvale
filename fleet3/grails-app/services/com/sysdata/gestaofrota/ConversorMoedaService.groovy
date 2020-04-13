package com.sysdata.gestaofrota

class ConversorMoedaService {

    BigDecimal paraReal(String valor) {
        valor = valor.replace("R\$", "").replace('.', '').replace(',', '.').trim()
        return new BigDecimal(valor)
    }
}
