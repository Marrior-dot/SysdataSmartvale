package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import com.sysdata.gestaofrota.Estabelecimento
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.integracao.omie.OmieCliente
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class OmieIntegradorNotaFiscalService implements GeradorNotaFiscal {


    GrailsApplication grailsApplication

    private OmieCliente recuperarClienteEmOmie(Estabelecimento estabelecimento) {

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        def filtro = [:]
        filtro.with {
            call = "ListarClientesResumido"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }

        filtro.clientesFiltro = [:]
        filtro.clientesFiltro.cnpj_cpf = estabelecimento.cnpj

        ResponseData responseData = RESTClientHelper.instance.postJSON(omieConfig.clientes.endpoint, filtro)



        OmieCliente omieCliente = new OmieCliente()

        return omieCliente
    }


    @Override
    void gerarNotaFiscal(Fatura fatura) {

        Estabelecimento estabelecimento = fatura.conta.participante
        OmieCliente omieCliente = OmieCliente.findByEstabelecimento(estabelecimento)

        // Caso não se encontre o Omie Cliente relacionado ao EC, faz-se nova consulta cliente no Omie
        if (!omieCliente)
            omieCliente = recuperarClienteEmOmie(estabelecimento)

        // Criar Ordem de Serviço

        def dataMap = [:]

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        dataMap.with {
            call = "IncluirOS"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }

        def param = [:]

        param.Cabecalho = [:]
        param.Cabecalho.with {
            cCodIntOS = fatura.id
            cEtapa = "20"
            dDtPrevisao = fatura.dataVencimento.format('dd/MM/yyyy')
            nCodCli = omieCliente.codigoIntegracao
            nQtdeParc = 0

        }
        param.Departamentos = []
        param.Email = [:]
        param.Email.with {
            cEnvBoleto = "N"
            cEnvLink = "S"
            cEnviarPara = estabelecimento.email
        }
        param.InformacoesAdicionais = []
        param.ServicosPrestados = [:]
        param.ServicosPrestados.with {
            cCodServLC116 = "10.05"
            cCodServMun = "82.99-7-02"
            cDadosAdicItem = "Serviços prestados"
            cDescServ = "SERVIÇO DE GERENCIAMENTO DO ABASTECIMENTO DA FROTA DE VEÍCULOS ATRAVÉS DE CARTÃO ELETRÔNICO"
            cRetemISS = "N"
            cTribServ = "01"
            nQtde = 1
            nValUnit = fatura.valorTotal
        }
        param.ServicosPrestados.impostos = [:]

        param.ServicosPrestados.impostos {

            cFixarCOFINS = ""
            cFixarCSLL = ""
            cFixarIRRF = ""
            cFixarISS = ""
            cFixarPIS = ""
            nAliqCOFINS = ""
            nAliqCSLL = 0
            nAliqIRRF = 0
            nAliqISS = 5
            nAliqPIS = 0
            nBaseISS = 0
            nTotDeducao = 0
            nValorCOFINS = 0
            nValorCSLL = 0
            nValorIRRF = 0
            nValorISS = 0
            nValorPIS = 0
        }

        dataMap.param = param

        ResponseData responseData = RESTClientHelper.instance.postJSON(omieConfig.ordemServico.endpoint, dataMap)

        if (responseData.statusCode == 200) {



        } else {

        }

    }
}
