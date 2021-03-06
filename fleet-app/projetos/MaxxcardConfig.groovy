import com.sysdata.gestaofrota.Banco
import com.sysdata.gestaofrota.TipoAdministradoraCartao
import com.sysdata.gestaofrota.TipoEmbossadora
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaAdministracao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaManutencao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaUtilizacao

/**
 * ESSE ARQUIVO DEVE SER IGNORADO PELO GIT
 * Cada projeto terá suas proprias variaveis
 */

environments {
    development {
        nome = "Maxxcard"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradora = TipoAdministradoraCartao.MAXCARD
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
        url = "jdbc:postgresql://148.5.7.216/maxxcard_homologation"
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

administradora{

    nome="MAXXCARD ADMINISTRADORA DE CARTOES LTDA"
    cnpj="12.387.832/0001-91"

    contaBancaria{
        banco="BANCO_ITAU"
        conta="14722"
        contadv="2"
        carteira=6
        agencia="716"
        agenciadv="2"
    }
}


faturamento{
    controlaSaldo=true
    extensoes=[TaxaUtilizacao,TaxaManutencao,TaxaAdministracao]
}