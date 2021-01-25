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
        url = "jdbc:postgresql://172.16.100.12/banparafrota_production"
        username = "banparafrota_production"
        password = "7xAh4RWyLrzUDsuh"

        /**
         * o nome contido na variável 'folder' deve ser o mesmo nome da pasta dentro do
         * diretório web-app/images/projetos. Essa pasta deve conter as imagens: icon, logo, logo-small
         */
        pasta = "banpara"
        //geradorCartao = NewGeradorCartaoService
        corPrimaria = "#D9241B"
        corSecundaria = "#28156C"

        context = "/banpara-frota"
    }
}

administradora {
    nome = "BANPARA"
    bin = "530370"
    anosValidadeCartao = 2
}

cartao {
    gerador = GeradorCartaoPadrao

    embossing {
        produto = "BANPARA FROTA"
        idCliente = "BANPR"

        maximoColunasLinhaEmbossing = 27
        gerador = "geracaoEmbossingBanparaIntelcavService"

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

        cartao.embossing.cipher.combinedKey = "C2385BF4F73B7029D6A410529101D3C2"

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    autenticar {
                        endpoint = "https://eaa6b7a5-9b4a-4159-a105-6135eb7dbfeb.mock.pstmn.io/controlecanais/v1/autenticar"
                        usuario = "teste"
                        chave = "teste"
                    }

                    lote {
                        endpoint = "https://eaa6b7a5-9b4a-4159-a105-6135eb7dbfeb.mock.pstmn.io/transferencia/contacorrente/v1/lote"
                        operador = "00000042"
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

            intelcav {
                dir {
                    embossing = "intelcav/embossing/"
                }
            }
        }
    }

    homologation {

        cartao.embossing.cipher.combinedKey = "C2385BF4F73B7029D6A410529101D3C2"

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    jksFile = "/usr/local/frota/banpara/banparakeystore.jks"
                    password = "sysdata"

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
            intelcav {
                dir {
                    embossing = "intelcav/embossing/"
                }
            }
        }
    }

    production {

        cartao.embossing.cipher.combinedKey = "C2385BF4F73B7029D6A410529101D3C2"

        reembolso {
            banpara {
                contaDebito {
                    agencia = "49"
                    conta = "6491626"
                }

                api {

                    jksFile = "/usr/local/frota/banpara/banparakeystore.jks"
                    password = "sByasNdPartaa"

                    autenticar {
                        //endpoint = "https://172.35.21.30/APIControleCanais/v1/autenticar"
                        endpoint = "https://srvwspa02/APIControleCanais/v1/autenticar"
                        usuario = "GSMB"
                        chave = "a9cUGGf2thbc6HUCZeyWchK4BsEVrxmuNRqKasF4CvN3mjt4Wce9GmqtqhmkAm8HJgoV2VbRgrpLqZXJb2hzaP7oTZ9AjoboujvCRS8ads5i9b7nLCXkcXVtog9DiwMkJaOsMJpFyc9hiaVjE6P8cjjpkjfWCfPjAbib9u2PRP3gDrePzPiHtq4Y5Nw4Mw34P8xgi6jExcqTez73hdyDSc2WAGVqVPNi4K6owHOsbnr3teWH6dUiWvEqe82zfgTd"
                    }

                    lote {
                        //endpoint = "https://172.35.21.30/transferencia/contacorrente/v1/lote"
                        endpoint = "https://srvwspa02/transferencia/contacorrente/v1/lote"
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
            intelcav {
                dir {
                    embossing = "intelcav/embossing/"
                }
            }
        }
    }

}