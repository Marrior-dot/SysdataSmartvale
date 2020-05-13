import com.sysdata.gestaofrota.TipoAdministradoraCartao
import com.sysdata.gestaofrota.TipoEmbossadora
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaAdministracao
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaManutencao
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaUtilizacao

/**
 * ESSE ARQUIVO DEVE SER IGNORADO PELO GIT
 * Cada projeto terá suas proprias variaveis
 */

environments {
    development {
        nome = "BahiaVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART


        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = System.getenv("FROTA_DEV_DB") ?: "jdbc:postgresql://172.17.0.2/bahiavale_development"

        username = "postgres"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "bahiavale"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#BEBEBE"
        corSecundaria = "#696969"

        context = "/bahiavale-frota"
    }

    homologation {
        nome = "BahiaVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://192.168.250.41/bahiavale_homologation"
        username = "postgres"
        password = "postgres"


        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "bahiavale"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#BEBEBE"
        corSecundaria = "#696969"

        context = "/bahiavale-hom"
    }
}

administradora {
    nome = "BAHIA VALE"
    bin = "605482"
    anosValidadeCartao = 2

}

embossing {
    produto = "BAHIA VALE"
    idCliente = "BAHVLE"
}


processamentos = [
        "faturamentoService",
        "geracaoArquivoCobrancaService"
]


faturamento {
    controlaSaldo = true
    extensoes = [TaxaUtilizacao, TaxaManutencao, TaxaAdministracao]
}