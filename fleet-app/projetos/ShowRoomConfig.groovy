import com.sysdata.gestaofrota.proc.cartao.GeradorCartaoPadrao
import com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento.Anuidade
import com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento.TarifaBancaria
import com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento.TaxaAdesao
import com.sysdata.gestaofrota.proc.faturamento.ext.estabelecimento.TaxaVisibilidade
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaAdministracao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaManutencao
import com.sysdata.gestaofrota.proc.faturamento.ext.portador.TaxaUtilizacao

/**
 * ESSE ARQUIVO DEVE SER IGNORADO PELO GIT
 * Cada projeto terá suas proprias variaveis
 */


projectId = "showroom"

environments {
    development {
        nome = "ShowRoom"
        tipoPrograma = 7
        parceiro = 2


        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://172.17.0.2/showroom_development"

        username = "postgres"
        password = "postgres"

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "showroom"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#04A34D"
        corSecundaria = "#A4DB05"

        context = "/showroom-dev"
    }

    homologation {
        nome = "ShowRoom"
        tipoPrograma = 7
        parceiro = 2

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://192.168.250.41/showroom"
        username = "postgres"
        password = "postgres"



        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "showroom"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#07A75A"
        corSecundaria = "#696969"

        context = "/showroom"
    }



}


administradora {
    nome = "Sysdata Sistemas Integrados"
    bin = "605009"
    anosValidadeCartao = 2

    endereco {
        cep = "44051-900"
        logradouro = "AV GOVERNADOR JOÃO DURVAL CARNEIRO"
        numero = "3665"
        complemento = "ED MULTIPLACE SALA 915"
        bairro = "SÃO JOAO"
        cidade = "Feira de Santana"
        estado = "BA"
    }


}

cartao {
    gerador = GeradorCartaoPadrao

    embossing {
        produto = "SYSDATA"
        idCliente = "SYSDATA"

        enviarPaysmart = true

        maximoColunasLinhaEmbossing = 24
        gerador = "geracaoEmbossingPaysmartService"
    }
}

faturamento {

    portador {

        controlaSaldo = true

        extensoes = [TaxaUtilizacao, TaxaManutencao, TaxaAdministracao]

    }

    estabelecimento {
        extensoes = [TaxaAdesao, TaxaVisibilidade, Anuidade, TarifaBancaria]
    }

}


environments {

    development {

        //cartao.embossing.cipher.combinedKey = "A7DAA1324C623EF2CB70704CC4D3F249"
        cartao.embossing.cipher.combinedKey = "9DDCF7CDC1B96725835D58AB404C62D0"

        sftp {
            host = "localhost"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'

        }

        arquivos {
            baseDir = "/home/luiz/tmp/frota/showroom/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_test/input"
                }
            }

            boleto {
                dir {
                    prepago = "boletos/prepago/"
                    pospago = "boletos/pospago/"
                }
            }

            cobranca {
                dir = "cobranca/"
            }

        }
    }

    homologation {

        cartao.embossing.cipher.combinedKey = "9DDCF7CDC1B96725835D58AB404C62D0"

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/showroom/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_test/input"
                }
            }

            boleto {
                dir {
                    prepago = "boletos/prepago/"
                    pospago = "boletos/pospago/"
                }
            }

            cobranca {
                dir = "cobranca/"
            }
        }
    }

}