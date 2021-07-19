import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.TipoAdministradoraCartao
import grails.util.Holders


fixture {
    println("CRIANDO ADMINISTRADORA...")
    if (Holders.config.tipoAdministradoraCartao == TipoAdministradoraCartao.MAXCARD)
        partAdministradora(Administradora, nome: "MAXXCARD", bin: "636557")

    println("CRIANDO PROCESSADORA...")
    partProcessadora(Processadora, nome: "SYSDATA")
}