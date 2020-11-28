package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import com.sysdata.commons.IntegrationMessengerService
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.integracao.omie.OmieCliente
import com.sysdata.gestaofrota.integracao.omie.OmieOrdemServico
import com.sysdata.gestaofrota.integracao.omie.OmieStatusOrdemServico
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class OmieIntegradorNotaFiscalService implements GeradorNotaFiscal {


    GrailsApplication grailsApplication

    IntegrationMessengerService integrationMessengerService

    private OmieCliente recuperarClienteEmOmie(Rh empresaCliente) {

        OmieCliente omieCliente

        def omieConfig = grailsApplication.config.projeto.faturamento.portador.notaFiscal.omie

        d

        def filtro = [:]
        filtro.with {
            call = "ListarClientesResumido"
            app_key = omieConfig.chavesAcesso.appKey
            app_secret = omieConfig.chavesAcesso.appSecret
        }

        filtro.param = [ [clientesFiltro: [cnpj_cpf: empresaCliente.cnpj]] ]

        ResponseData responseData = integrationMessengerService.postAsJSON(omieConfig.clientes.endpoint, TipoMensagem.OMIE_CONSULTAR_CLIENTES, filtro)
        if (responseData.statusCode == 200) {

            responseData.json.clientes_cadastro_resumido.find {
                omieCliente = OmieCliente.findByCodigoIntegracao(it.codigo_cliente)
                if (!omieCliente) {
                    omieCliente = new OmieCliente(empresaCliente: empresaCliente, codigoIntegracao: it.codigo_cliente)
                    omieCliente.save()
                }
                return true
            }

        //TODO: Tratar resposta negativa
        } else if (responseData.statusCode == 500) {

            log.debug "Erro: $responseData.json"
/*
            {
                "faultstring": "ERROR: Não existem registros para a página [1]!",
                "faultcode": "SOAP-ENV:Client-5113"
            }
*/
        }

        return omieCliente
    }


    private void incluirOrdemServicoEmOmie(OmieCliente omieCliente, Fatura fatura) {

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

        ResponseData responseData = integrationMessengerService.postAsJSON(omieConfig.ordemServico.endpoint, TipoMensagem.OMIE_INCLUIR_OS, dataMap)

        if (responseData.statusCode == 200) {


/*
            {
                "cCodIntOS": "1606502110",
                "nCodOS": 488948625,
                "cNumOS": "000000000002117",
                "cCodStatus": "0",
                "cDescStatus": "Ordem de Serviço cadastrado com sucesso!"
            }
*/

            OmieStatusOrdemServico omieStatusOS = OmieStatusOrdemServico.findByCodigo(responseData.json.cCodStatus)
            if (!omieStatusOS) {
                omieStatusOS = new OmieStatusOrdemServico()
                omieStatusOS.with {
                    codigo = responseData.json.cCodStatus
                    descricao = responseData.json.cCodStatus
                }
                omieStatusOS.save(flush: true)
            }


            OmieOrdemServico ordemServico = new OmieOrdemServico(fatura: fatura)
            ordemServico.with {
                cliente = omieCliente
                codigoOs = responseData.json.nCodOS
                numeroOs = responseData.json.cNumOS
                statusOs = omieStatusOS
            }

            ordemServico.save(flush: true)

            // TODO: Tratar resposta negativa
        } else if (responseData.statusCode == 500) {

            log.debug "Erro: $responseData.json"

        }

    }

    @Override
    void gerarNotaFiscal(Fatura fatura) {

        Rh empresaCliente = fatura.conta.participante
        OmieCliente omieCliente = OmieCliente.findByEmpresaCliente(empresaCliente)

        // Caso não se encontre o Omie Cliente relacionado ao EC, faz-se nova consulta cliente no Omie
        if (!omieCliente)
            omieCliente = recuperarClienteEmOmie(empresaCliente)

        if (omieCliente)
            incluirOrdemServicoEmOmie(omieCliente, fatura)
    }
}
