package com.sysdata.gestaofrota.processamento

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Portador

/**
 * Created by hyago on 05/10/17.
 */
interface IGeradorCartao {
    String gerarNumero(Administradora administradora, Portador portador)
    String gerarSenha()
    Date gerarDataValidade()
    String gerarCVV()
    String calcularDV(String numero)
}