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
        nome = "Maxxcard"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.MAXCARD
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = System.getenv("FROTA_DEV_DB") ?: "jdbc:postgresql://localhost/maxxcard_development"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "maxxcard"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#f63535"
        corSecundaria = "#f2b941"
    }

    homologation {
        nome = "Maxxcard"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.MAXCARD
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://148.5.7.216/maxxcard_development"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "maxxcard"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#f63535"
        corSecundaria = "#f2b941"
    }
}

administradora {

    nome="MAXXCARD ADMINISTRADORA DE CARTOES LTDA"
    cnpj="12.387.832/0001-91"

    contaBancaria {
        banco=341
        agencia="7162"
        agenciaDv="0"
        numero="14722"
        numeroDv="2"
        carteira="109"
    }
}



processamentos=[
    "faturamentoService",
    "geracaoArquivoCobrancaService"
]




faturamento{
    controlaSaldo=true
    extensoes=[TaxaUtilizacao,TaxaManutencao,TaxaAdministracao]
}