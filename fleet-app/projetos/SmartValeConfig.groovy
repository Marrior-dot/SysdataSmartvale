import com.sysdata.gestaofrota.TipoAdministradoraCartao
import com.sysdata.gestaofrota.TipoEmbossadora
import com.sysdata.gestaofrota.proc.cartao.GeradorCartaoPadrao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaAdministracao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaManutencao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaUtilizacao

/**
 * ESSE ARQUIVO DEVE SER IGNORADO PELO GIT
 * Cada projeto terá suas proprias variaveis
 */


projectId = "smartvale"

environments {
    development {
        nome = "SmartVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART


        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = System.getenv("FROTA_DEV_DB") ?: "jdbc:postgresql://172.17.0.2/smartvale_development"

        username = "postgres"
        password = "postgres"
        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "smartvale"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#07A75A"
        corSecundaria = "#696969"

        context = "/smartvale-frota"
    }

    homologation {
        nome = "SmartVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://192.168.250.41/smartvale_homologation"
        username = "postgres"
        password = "postgres"

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "smartvale"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#07A75A"
        corSecundaria = "#696969"

        context = "/smartvale-hom"
    }
}

administradora {
    nome = "SMART VALE"
    bin = "609095"
    anosValidadeCartao = 2

}

cartao {
    gerador = GeradorCartaoPadrao
    embossing {
        produto = "SMART VALE"
        idCliente = "SVALE"
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



environments {

    development {

        sftp {
            host = "localhost"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'

        }

        arquivos {
            baseDir = "/home/luiz/tmp/frota/smartvale/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_test/input"
                }
            }
        }
    }

    homologation {

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/smartvale/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_test/input"
                }
            }
        }
    }

    production {

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/smartvale/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_prod/input"
                }
            }
        }
    }

}