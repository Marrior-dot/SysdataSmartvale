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


projectId = "banpara"

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

        corPrimaria = "#D9241B"
        corSecundaria = "#28156C"

        loginTransparente = true

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
        url = "jdbc:postgresql://192.168.250.41/banpara_homologation"
        username = "postgres"
        password = "postgres"

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "banpara"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#D9241B"
        corSecundaria = "#28156C"

        context = "/banpara-hom"
    }

    production {
        nome = "Banpara"
        tipoPrograma = 7
        parceiro = 2
        tipoAdministradoraCartao = TipoAdministradoraCartao.BANPARA
        tipoEmbossadora = TipoEmbossadora.PAYSMART

        // ** DATABASE **
        dbCreate = "update" // one of 'create', 'create-drop','update'
        url = "jdbc:postgresql://192.168.250.41/banpara_production"
        username = "banpara_production"
        password = "postgres"

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "banpara"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#D9241B"
        corSecundaria = "#28156C"

        context = "/banpara-hom"
    }
}

administradora {
    nome = "BANPARA"
    bin = "637233"
    anosValidadeCartao = 2

}

cartao {
    gerador = GeradorCartaoPadrao
    embossing {
        produto = "BANPARA FROTA"
        idCliente = "BANPR"
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

        cartao.embossing.cipher.combinedKey = "9DDCF7CDC1B96725835D58AB404C62D0"

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    autenticar {
                        baseUrl = "https://eaa6b7a5-9b4a-4159-a105-6135eb7dbfeb.mock.pstmn.io"
                        usuario = "teste"
                        chave = "teste"
                    }

                    transferencia {
                        baseUrl = "https://eaa6b7a5-9b4a-4159-a105-6135eb7dbfeb.mock.pstmn.io"
                    }

                }
            }
        }

        sftp {
            host = "localhost"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'

        }

        arquivos {
            baseDir = "/home/luiz/tmp/frota/banpara/"
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

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    jksFile = "/usr/local/frota/banpara/banparakeystore.jks"

                    autenticar {
                        endpoint = "https://172.35.21.19/controlecanais/v1/autenticar"
                        usuario = "GSMB"
                        chave = "a9cUGGf2thbc6HUCZeyWchK4BsEVrxmuNRqKasF4CvN3mjt4Wce9GmqtqhmkAm8HJgoV2VbRgrpLqZXJb2hzaP7oTZ9AjoboujvCRS8ads5i9b7nLCXkcXVtog9DiwMkJaOsMJpFyc9hiaVjE6P8cjjpkjfWCfPjAbib9u2PRP3gDrePzPiHtq4Y5Nw4Mw34P8xgi6jExcqTez73hdyDSc2WAGVqVPNi4K6owHOsbnr3teWH6dUiWvEqe82zfgTd"
                    }

                    lote {
                        endpoint = "https://172.35.21.19/transferencia/contacorrente/v1/lote"
                        operador = "00000042"
                    }
                }
            }
        }

        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/banpara/"
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

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    jksFile = "/usr/local/frota/banpara/banparakeystore.jks"

                    autenticar {
                        endpoint = "https://172.35.21.19/controlecanais/v1/autenticar"
                        usuario = "GSMB"
                        chave = "a9cUGGf2thbc6HUCZeyWchK4BsEVrxmuNRqKasF4CvN3mjt4Wce9GmqtqhmkAm8HJgoV2VbRgrpLqZXJb2hzaP7oTZ9AjoboujvCRS8ads5i9b7nLCXkcXVtog9DiwMkJaOsMJpFyc9hiaVjE6P8cjjpkjfWCfPjAbib9u2PRP3gDrePzPiHtq4Y5Nw4Mw34P8xgi6jExcqTez73hdyDSc2WAGVqVPNi4K6owHOsbnr3teWH6dUiWvEqe82zfgTd"
                    }

                    lote {
                        endpoint = "https://172.35.21.19/transferencia/contacorrente/v1/lote"
                        operador = "00000042"
                    }
                }
            }
        }


        sftp {
            host = "172.17.17.2"
            port = 22
            user = 'sysdata'
            pswd = 'ldAFWzWLA85i3XWP'
            privateKeyFile = "/usr/local/frota/bahiavale/.ssh/id_rsa"
        }

        arquivos {
            baseDir = "/usr/local/frota/banpara/"
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