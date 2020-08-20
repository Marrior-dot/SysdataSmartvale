import com.sysdata.gestaofrota.TipoAdministradoraCartao
import com.sysdata.gestaofrota.TipoEmbossadora
import com.sysdata.gestaofrota.proc.cartao.GeradorCartaoPadrao
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaAdministracao
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaManutencao
import com.sysdata.gestaofrota.proc.faturamento.ext.TaxaUtilizacao

/**
 * ESSE ARQUIVO DEVE SER IGNORADO PELO GIT
 * Cada projeto terá suas proprias variaveis
 */

projectId = "bahiavale"

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
        corPrimaria = "#3E0090"
        corSecundaria = "#00993E"

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
        corPrimaria = "#3E0090"
        corSecundaria = "#00993E"

        context = "/bahiavale-hom"
    }

    production {
        nome = "BahiaVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://172.16.100.12:5432/bvalefrota_production"
        username = "bvalefrota_production"
        password = "E934X8uIo68Fw5d"


        // ** DATABASE **

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "bahiavale"
        corPrimaria = "#3E0090"
        corSecundaria = "#00993E"

        context = "/bahiavale-hom"
    }
}

administradora {
    nome = "BAHIA VALE"
    bin = "605482"
    anosValidadeCartao = 2

}

cartao {
    gerador = GeradorCartaoPadrao
    embossing {
        produto = "BAHIA VALE"
        idCliente = "BVALE"
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
            baseDir = "/home/luiz/tmp/frota/bahiavale/"
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
        arquivos {
            baseDir = "/usr/local/frota/bahiavale"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "/home/luiz/tmp/paysmart/ftp/entrada"
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
        }

        arquivos {
            baseDir = "/usr/local/frota/bahiavale"
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