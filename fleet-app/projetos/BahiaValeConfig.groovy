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

projectId = "bahiavale"

environments {

    test {
        dbCreate = "update"
        url = "jdbc:postgresql://172.17.0.2/bahiavale_development"
        username = "postgres"
        password = "postgres"
    }

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
        corPrimaria = "#210E7F"
        corSecundaria = "#E8022C"

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
        corPrimaria = "#210E7F"
        corSecundaria = "#E8022C"

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
        corPrimaria = "#210E7F"
        corSecundaria = "#E8022C"

        context = "/bahiavale-hom"
    }
}

administradora {
    nome = "BAHIA VALE"
    cnpj = "30379128000179"
    bin = "605482"
    inscricaoMunicipal = "4944886"
    cnae = "6499904"
    anosValidadeCartao = 2

    endereco {
        cep = "41820-020"
        logradouro = "Avenida Tancredo Neves"
        numero = "1632"
        complemento = "Salvador Trade Center, torre norte sala 801"
        bairro = "Caminho das Árvores"
        cidade = "Salvador"
        estado = "BA"
    }

    endereco_notafiscal {
        cep = "41820-020"
        logradouro = "Avenida Tancredo Neves"
        numero = "1632"
        complemento = "Salvador Trade Center, torre norte sala 801"
        bairro = "Caminho das Árvores"
        cidade = "Salvador"
        estado = "BA"
    }


}

cartao {
    gerador = GeradorCartaoPadrao
    embossing {
        produto = "BAHIA VALE"
        idCliente = "BVALE"
    }
}

faturamento {

    portador {

        controlaSaldo = true

        extensoes = [TaxaUtilizacao, TaxaManutencao, TaxaAdministracao]

        boleto {
            gerar = true
            //gerador = "geradorBoletoBancoBrasilService"
            gerador = "bopepoGeradorBoletoBancoBrasilService"

            agencia = "3454"
            dvAgencia = "1"
            conta =  "38577"
            dvConta = "8"

            carteira {
                numero = "17"
                variacao = "019"
            }

            convenio = "3215085"
            contrato = "19997095"

            instrucao1 = "Referente ao serviço de fornecimento de vale combustível para o abastecimento da frota"
            instrucao2 = ""
        }

        notaFiscal {

            descriminacaoServicos = """SERVIÇO DE GERENCIAMENTO DO ABASTECIMENTO DA FROTA DE VEÍCULOS ATRAVÉS|DE CARTÃO ELETRÔNICO||VALOR CONSUMIDO: ${valor}||TAXA DE ADMINISTRAÇÃO: ${taxa}||VALOR FINAL: ${total}|"""
        }


    }

    estabelecimento {
        extensoes = [TaxaAdesao, TaxaVisibilidade, Anuidade, TarifaBancaria]
    }

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

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/bahiavale/"
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

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/bahiavale/"
            paysmart {
                dir {
                    saida   = "paysmart/saida/"
                    enviado = "paysmart/enviado/"
                    enviar  = "paysmart_prod/input"
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