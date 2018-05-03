package com.sysdata.gestaofrota.proc.faturamento

import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.StatusBoleto
import com.sysdata.gestaofrota.proc.Processamento
import com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240.Cnab400Arquivo

class GeracaoArquivoCobrancaService implements Processamento {


    @Override
    def executar(Date dataProc) {

        log.info "Iniciando Geracao Arquivo Remessa..."

        def boletos=Boleto.findAllByStatus(StatusBoleto.CRIADO)

        if(boletos){
            Cnab400Arquivo.makeHeader{
                agencia=""
                conta=""
                contaDv=""
                nomeEmpresa=""
                codigoBanco=""
                nomeBanco=""
                dataGeracao=dataProc
            }

            boletos.each{

            }

            Cnab400Arquivo.makeTrailer{

            }

        }

        log.info "Geracao Arquivo Remessa finalizada"

    }
}
