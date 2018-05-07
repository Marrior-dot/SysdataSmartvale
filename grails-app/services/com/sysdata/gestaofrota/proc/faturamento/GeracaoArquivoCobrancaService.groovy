package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.Arquivo
import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusArquivo
import com.sysdata.gestaofrota.StatusBoleto
import com.sysdata.gestaofrota.TipoArquivo
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.proc.Processamento
import com.sysdata.gestaofrota.proc.cobrancaBancaria.BancoCobranca
import com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240.Cnab400Arquivo
import com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240.Cnab400Detalhe
import com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240.Cnab400Header
import com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240.Cnab400Trailer

class GeracaoArquivoCobrancaService implements Processamento {

    def grailsApplication

    @Override
    def executar(Date dataProc) {

        log.info "Iniciando Geracao Arquivo Remessa..."

        BancoCobranca bancoCobranca=BancoCobranca.factoryMethod(grailsApplication.config.project.administradora.contaBancaria.banco)

        def cnpjAdm=grailsApplication.config.project.administradora.cnpj
        def agenciaAdm=grailsApplication.config.project.administradora.contaBancaria.agencia
        def contaAdm=grailsApplication.config.project.administradora.contaBancaria.numero
        def numDv=grailsApplication.config.project.administradora.contaBancaria.numeroDv

        def boletos=Boleto.findAllByStatus(StatusBoleto.CRIADO)

        if(boletos){

            StringBuilder sbFile=new StringBuilder()

            Cnab400Header header=Cnab400Arquivo.makeHeader{
                tipoRegistro="0"
                operacao="1"
                literalRemessa="REMESSA"
                codigoServico="01"
                literalServico="COBRANCA"
                filler1="00"
                agencia=agenciaAdm as int
                conta=contaAdm as int
                contaDv=numDv as int
                filler2=""
                nomeEmpresa=grailsApplication.config.project.administradora.nome
                codigoBanco=bancoCobranca.codigoCompensacao
                nomeBanco=bancoCobranca.nome
                filler3=""
                dataGeracao=dataProc
            }

           // println header.campos

            sbFile.append(header.flatten())

            def details=[]

            boletos.each{b->

                Rh rh=b.fatura.conta.participante as Rh

                Cnab400Detalhe detail=Cnab400Arquivo.makeDetail{
                    tipoRegistro="1"
                    codigoInscricao="02"
                    numeroInscricao=rh.cnpj?Util.cnpjToRaw(rh.cnpj) as long:0
                    agencia=agenciaAdm
                    filler1="00"
                    conta=contaAdm
                    dac=numDv
                    instrucao="0000"
                    usoEmpresa=""
                    filler2="    "
                    nossoNumero=b.nossoNumero.toLong()
                    qtdeMoeda=0
                    numeroCarteira=grailsApplication.config.project.administradora.contaBancaria.carteira as int
                    usoBanco=""
                    carteira="1"
                    codigoOcorrencia="01"
                    numeroDocumento=b.titulo
                    vencimento=b.dataVencimento
                    valorTitulo=b.fatura.valorTotal*100 as int
                    codigoBanco=bancoCobranca.codigoCompensacao
                    agenciaCobradora="00000"
                    especie="01"
                    aceite="N"
                    dataEmissao=dataProc
                    instrucao1="05"
                    instrucao2="05"
                    juros1Dia=0
                    descontoAte="000000"
                    valorDesconto=0
                    valorIOF=0
                    abatimento=0
                    codInscPagador="02"
                    numInscPagador=Util.cnpjToRaw(rh.cnpj)
                    nomePagador=rh.nome
                    filler3=""
                    logradouroPagador=(rh.endereco?.logradouro)?:""
                    bairroPagador=(rh.endereco?.bairro)?:""
                    cepPagador=(rh.endereco?.cep)? rh.endereco?.cep.replace("-","") as int:0
                    cidadePagador=(rh.endereco?.cidade?.nome)?:""
                    estadoPagador=(rh.endereco?.cidade?.estado?.uf)?:""
                    sacadorAvalista=""
                    filler4="    "
                    dataMora="000000"
                    prazoDias="00"
                    filler5=" "
                }

                //println detail.campos

                sbFile.append(detail.flatten())

            }

            Cnab400Trailer trailer=Cnab400Arquivo.makeTrailer{
                tipoRegistro="9"
                filler=""
            }

            sbFile.append(trailer.flatten())

            //Salva arquivo no banco
            Arquivo arqRemessa=new Arquivo()
            def loteArq=Arquivo.nextLote(TipoArquivo.REMESSA_COBRANCA)
            arqRemessa.with{
                nome="remessa_${bancoCobranca.codigoCompensacao}_${loteArq}.rem"
                lote=loteArq
                status=StatusArquivo.GERADO
                conteudo=sbFile.toString()
                tipo=TipoArquivo.REMESSA_COBRANCA
            }
            arqRemessa.save flush:true

            //Boleto.executeUpdate("update Boleto set status=:newSt where status=:oldSt",[newSt:StatusBoleto.REMESSA,oldSt:StatusBoleto.CRIADO])


        }else
            log.info "Nao ha boletos para geracao de arquivo remessa"

        log.info "Geracao Arquivo Remessa finalizada"

    }
}
