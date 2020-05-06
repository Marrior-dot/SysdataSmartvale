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
        nome = "Banpara"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART


        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = System.getenv("FROTA_DEV_DB") ?: "jdbc:postgresql://172.17.0.2/banpara_development"

        username = "postgres"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "banpara"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#BEBEBE"
        corSecundaria = "#696969"

        context = "/banpara-frota"
    }

    homologation {
        nome = "Banpara"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://192.168.250.41/maxxcard_homologation"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "banpara"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#BEBEBE"
        corSecundaria = "#696969"
    }
}

administradora {
    nome = "BANPARA"
    bin = "605009"
    anosValidadeCartao = 2

    contaBancaria {
        banco = 341
        agencia = "7162"
        agenciaDv = "0"
        numero = "14722"
        numeroDv = "2"
        carteira = "109"

        boleto {
            localPagamento = "Pagável em qualquer Banco até a data de vencimento"
            instrucao1 = "Aceitar até a data de vencimento"
            instrucao2 = "Após o vencimento aceito apenas nas agências do ITAU"
        }
    }
}

processamentos = [
        "faturamentoService",
        "geracaoArquivoCobrancaService"
]


faturamento {
    controlaSaldo = true
    extensoes = [TaxaUtilizacao, TaxaManutencao, TaxaAdministracao]
}