import com.sysdata.gestaofrota.TipoAdministradoraCartao
import com.sysdata.gestaofrota.TipoEmbossadora
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
        corPrimaria = "#04A34D"
        corSecundaria = "#A4DB05"

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

    production {
        nome = "SmartVale"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://172.16.100.12:5432/smartvale_production"
        username = "smartvale_production"
        password = "EC5Fsu3X6hSw/nqk"


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


}

administradora {
    nome = "SMART VALE"
    bin = "609095"
    cnpj = "23685734000157"

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
        produto = "SMART VALE"
        idCliente = "SVALE"

        enviarPaysmart = true

        maximoColunasLinhaEmbossing = 24
        gerador = "geracaoEmbossingPaysmartService"
    }
}

faturamento {

    portador {

        controlaSaldo = true

        extensoes = [TaxaUtilizacao, TaxaManutencao, TaxaAdministracao]

        boleto {
            gerar = false
            gerador = "bopepoGeradorBoletoBancoBrasilService"

            agencia = "4494"
            dvAgencia = "6"
            conta =  "14905"
            dvConta = "5"

            carteira {
                numero = "17"
                variacao = "019"
            }

            convenio = "3324913"

            instrucao1 = "Referente ao serviço de fornecimento de vale combustível para o abastecimento da frota"
            instrucao2 = ""
        }

        notaFiscal {

            gerar = false
            gerador = "omieIntegradorNotaFiscalService"

            omie {

                chavesAcesso {
                    appKey = "1252908413757"
                    appSecret = "bbb0fddfb5ebb06f59aad5fc4ccc2a57"
                }

                clientes {
                    endpoint = "https://app.omie.com.br/api/v1/geral/clientes/"
                }

                categorias {
                    endpoint = "https://app.omie.com.br/api/v1/geral/categorias/"
                }

                ordemServico {
                    endpoint = "https://app.omie.com.br/api/v1/servicos/os/"
                }

                ordemServicoFaturamento {
                    endpoint = "https://app.omie.com.br/api/v1/servicos/osp/"
                }

            }

            //descriminacaoServicos = 'SERVIÇO DE GERENCIAMENTO DO ABASTECIMENTO DA FROTA DE VEÍCULOS ATRAVÉS|DE CARTÃO ELETRÔNICO||VALOR CONSUMIDO: ${valorConsumido}||${taxas}||VALOR FINAL: ${valorTotal}|'
        }

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
            baseDir = "/home/luiz/tmp/frota/smartvale/"
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
            baseDir = "/usr/local/frota/smartvale/"
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

    production {

        cartao.embossing.cipher.combinedKey = "9DDCF7CDC1B96725835D58AB404C62D0"

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