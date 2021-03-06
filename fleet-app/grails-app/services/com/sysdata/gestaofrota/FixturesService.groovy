package com.sysdata.gestaofrota

import com.fourLions.processingControl.BatchProcessing
import com.fourLions.processingControl.DailySchedule
import com.fourLions.processingControl.ExecutionFrequency
import com.fourLions.processingControl.Processing
import com.sysdata.commons.dates.Holiday
import com.sysdata.commons.dates.HolidayDate
import com.sysdata.commons.dates.MunicipalHoliday
import com.sysdata.commons.dates.MunicipalHolidayDate
import com.sysdata.commons.dates.NationalHoliday
import com.sysdata.commons.dates.NationalHolidayDate
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class FixturesService {

    GrailsApplication grailsApplication

    def init() {
        criarPerfisAcesso()
        criarProcessadora()
        //criarAdministradora()
        criarAutenticacaoInicial()
        criarEstadosCapitais()
        criarBancos()
        criarProcessamentos()
        criarMarcasVeiculos()
        criarMotivosNegacao()
        criarFeriadosNacionais()
    }

    private void criarPerfisAcesso() {
        Role.findOrCreateWhere(authority: 'ROLE_PROC').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_ADMIN').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_RH').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_ESTAB').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_PROC_FINANC', description: 'Perfil do Financeiro Processadora').save(flush: true)
    }


    private def criarProcessadora() {
        Processadora.findOrCreateWhere(nome: "Sysdata Tecnologia").save(flush: true)
        assert Processadora.count() == 1
    }

    private def criarAdministradora() {
        def adm = grailsApplication.config.projeto.administradora
        Administradora.findOrCreateWhere([bin: adm.bin, nome: adm.nome]).save(flush: true)
        assert Administradora.count() == 1
        ParametroSistema.findOrCreateWhere([nome: ParametroSistema.ANOS_VALIDADE_CARTAO, valor: adm.anosValidadeCartao.toString(), escopo: EscopoParametro.ADMINISTRADORA, tipoDado: TipoDado.INTEGER]).save(flush: true)
    }


    private def criarAutenticacaoInicial() {
        Role startRole = Role.findOrCreateWhere(authority: "ROLE_PROC").save()
        User startUser
        if (! User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72", owner: Processadora.first())
            startUser.save()
            UserRole.create startUser, startRole
            log.info "Start User criado"
        }
    }

    private def criarEstadosCapitais() {
        Estado acre = Estado.findOrCreateWhere([uf: "AC", nome: "ACRE"]).save()
        Estado alagoas = Estado.findOrCreateWhere([uf: "AL", nome: "ALAGOAS"]).save()
        Estado amazonas = Estado.findOrCreateWhere([uf: "AM", nome: "AMAZONAS"]).save()
        Estado amapa = Estado.findOrCreateWhere([uf: "AP", nome: "AMAP??"]).save()
        Estado bahia = Estado.findOrCreateWhere([uf: "BA", nome: "BAHIA"]).save()
        Estado ceara = Estado.findOrCreateWhere([uf: "CE", nome: "CEAR??"]).save()
        Estado df = Estado.findOrCreateWhere([uf: "DF", nome: "DISTRITO FEDERAL"]).save()
        Estado espSanto = Estado.findOrCreateWhere([uf: "ES", nome: "ESP??RITO SANTO"]).save()
        Estado goias = Estado.findOrCreateWhere([uf: "GO", nome: "GOI??S"]).save()
        Estado maranhao = Estado.findOrCreateWhere([uf: "MA", nome: "MARANH??O"]).save()
        Estado minas = Estado.findOrCreateWhere([uf: "MG", nome: "MINAS GERAIS"]).save()
        Estado matoGrossoSul = Estado.findOrCreateWhere([uf: "MS", nome: "MATO GROSSO DO SUL"]).save()
        Estado matoGrosso = Estado.findOrCreateWhere([uf: "MT", nome: "MATO GROSSO"]).save()
        Estado para = Estado.findOrCreateWhere([uf: "PA", nome: "PAR??"]).save()
        Estado paraiba = Estado.findOrCreateWhere([uf: "PB", nome: "PARA??BA"]).save()
        Estado pernambuco = Estado.findOrCreateWhere([uf: "PE", nome: "PERNAMBUCO"]).save()
        Estado piaui = Estado.findOrCreateWhere([uf: "PI", nome: "PIAU??"]).save()
        Estado parana = Estado.findOrCreateWhere([uf: "PR", nome: "PARAN??"]).save()
        Estado rio = Estado.findOrCreateWhere([uf: "RJ", nome: "RIO DE JANEIRO"]).save()
        Estado rioGrandeNorte = Estado.findOrCreateWhere([uf: "RN", nome: "RIO GRANDE DO NORTE"]).save()
        Estado rondonia = Estado.findOrCreateWhere([uf: "RO", nome: "ROND??NIA"]).save()
        Estado rioGrandeSul = Estado.findOrCreateWhere([uf: "RS", nome: "RIO GRANDE DO SUL"]).save()
        Estado roraima = Estado.findOrCreateWhere([uf: "RR", nome: "RORAIMA"]).save()
        Estado santaCatarina = Estado.findOrCreateWhere([uf: "SC", nome: "SANTA CATARINA"]).save()
        Estado sergipe = Estado.findOrCreateWhere([uf: "SE", nome: "SERGIPE"]).save()
        Estado saoPaulo = Estado.findOrCreateWhere([uf: "SP", nome: "S??O PAULO"]).save()
        Estado tocantins = Estado.findOrCreateWhere([uf: "TO", nome: "TOCANTINS"]).save()

        Cidade.findOrCreateWhere([nome: "RIO BRANCO", estado: acre]).save()
        Cidade.findOrCreateWhere([nome: "MACEI??", estado: alagoas]).save()
        Cidade.findOrCreateWhere([nome: "MANAUS", estado: amazonas]).save()
        Cidade.findOrCreateWhere([nome: "MACAP??", estado: amapa]).save()
        Cidade.findOrCreateWhere([nome: "SALVADOR", estado: bahia]).save()
        Cidade.findOrCreateWhere([nome: "FORTALEZA", estado: ceara]).save()
        Cidade.findOrCreateWhere([nome: "BRAS??LIA", estado: df]).save()
        Cidade.findOrCreateWhere([nome: "VIT??RIA", estado: espSanto]).save()
        Cidade.findOrCreateWhere([nome: "GOI??NIA", estado: goias]).save()
        Cidade.findOrCreateWhere([nome: "S??O LU??S", estado: maranhao]).save()
        Cidade.findOrCreateWhere([nome: "BELO HORIZONTE", estado: minas]).save()
        Cidade.findOrCreateWhere([nome: "CAMPO GRANDE", estado: matoGrossoSul]).save()
        Cidade.findOrCreateWhere([nome: "CUIAB??", estado: matoGrosso]).save()
        Cidade.findOrCreateWhere([nome: "BEL??M", estado: para]).save()
        Cidade.findOrCreateWhere([nome: "JO??O PESSOA", estado: paraiba]).save()
        Cidade.findOrCreateWhere([nome: "RECIFE", estado: pernambuco]).save()
        Cidade.findOrCreateWhere([nome: "TERESINA", estado: piaui]).save()
        Cidade.findOrCreateWhere([nome: "CURITIBA", estado: parana]).save()
        Cidade.findOrCreateWhere([nome: "RIO DE JANEIRO", estado: rio]).save()
        Cidade.findOrCreateWhere([nome: "NATAL", estado: rioGrandeNorte]).save()
        Cidade.findOrCreateWhere([nome: "PORTO VELHO", estado: rondonia]).save()
        Cidade.findOrCreateWhere([nome: "PORTO ALEGRE", estado: rioGrandeSul]).save()
        Cidade.findOrCreateWhere([nome: "BOA VISTA", estado: roraima]).save()
        Cidade.findOrCreateWhere([nome: "FLORIAN??POLIS", estado: santaCatarina]).save()
        Cidade.findOrCreateWhere([nome: "ARACAJ??", estado: sergipe]).save()
        Cidade.findOrCreateWhere([nome: "S??O PAULO", estado: saoPaulo]).save()
        Cidade.findOrCreateWhere([nome: "PALMAS", estado: tocantins]).save()
    }

    private def criarBancos() {
        Banco.findOrCreateWhere([codigo: "1", nome: "BANCO DO BRASIL S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "104", nome: "CAIXA ECONOMICA FEDERAL"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "33", nome: "BANCO SANTANDER BRASIL S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "237", nome: "BANCO BRADESCO S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "341", nome: "BANCO ITAU S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "37", nome: "BANCO DO ESTADO DO PAR?? S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "748", nome: "BANCO COOPERATIVO SICRED S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "003", nome: "BANCO DA AMAZONIA S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "380", nome: "PICPAY SERVICOS S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "389", nome: "Banco Mercantil do Brasil S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "745", nome: "Banco Citibank S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "243", nome: "Banco M??xima S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "422", nome: "Banco Safra S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "623", nome: "Banco Pan S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "655", nome: "Banco Votorantim S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "069", nome: "Banco Crefisa S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "318", nome: "Banco BMG S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "070", nome: "BRB ??? Banco de Bras??lia S.A"]).save(flush: true)
    }

    private def criarProcessamentos() {
/*
        if (grailsApplication.config.projeto.projectId == "bahiavale")
            Processing.findOrCreateWhere([name: "Gera????o Arquivo NFe - RPS Barueri", order: 6 as byte, service: "geracaoArquivoRPSBarueriService", active: true, batch: batch]).save(flush: true)
*/

        if (grailsApplication.config.projeto.projectId == "banpara") {

            BatchProcessing batchCorte = BatchProcessing.findByName("Corte Di??rio")
            if (! batchCorte) {
                batchCorte = new BatchProcessing([name: "Corte Di??rio",
                                                  active: true,
                                                  executionSchedule: new DailySchedule(hour: 22,
                                                                                        minute: 0,
                                                                                        executionFrequency: ExecutionFrequency.DAILY)])
                batchCorte.save(flush: true)
            }

            Processing.findOrCreateWhere([name: "Agendamento de Transa????es", order: 1 as byte,
                                          service: "agendamentoTransacaoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Estabelecimento", order: 2 as byte,
                                          service: "corteReembolsoEstabsService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Fechamento de Lote Pagamento", order: 3 as byte,
                                          service: "fechamentoLotePagamentoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Fechamento de Lote Recebimento", order: 4 as byte,
                                          service: "fechamentoLoteRecebimentoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Envio Lote Recebimento Banpar?? API", order: 5 as byte,
                                          service: "enviarLoteRecebimentoAPIBanparaService", active: true,
                                          batch: batchCorte]).save(flush: true)


            BatchProcessing batch = BatchProcessing.findByName("Processamento Di??rio")
            if (! batch) {
                batch = new BatchProcessing([name: "Processamento Di??rio",
                                             active: true,
                                             executionSchedule: new DailySchedule(hour: 2, minute: 0, executionFrequency: ExecutionFrequency.DAILY)])
                batch.save(flush: true)
            }

            Processing.findOrCreateWhere([name: "Carga de Pedidos", order: 1 as byte,
                                          service: "cargaPedidoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Pedidos de Carga", order: 2 as byte,
                                          service: "faturamentoCargaPedidoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Portador", order: 3 as byte,
                                          service: "faturamentoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Gera????o Arquivo Embossing", order: 4 as byte,
                                          service: "geracaoArquivoEmbossingService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Atualiza????o de Saldos", order: 5 as byte,
                                          service: "atualizacaoSaldoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Consulta Lote Banpar?? API", order: 6 as byte,
                                          service: "consultarLoteAPIBanparaService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Consulta Lotes Devolvidos - Banpar?? API", order: 7 as byte,
                                          service: "consultarLoteDevolucaoAPIBanparaService", active: true,
                                          batch: batch]).save(flush: true)


            Processing envioLote = Processing.findOrCreateWhere([
                                            name: "Envio Lote Banpar?? API",
                                            order: 1 as byte,
                                            service: "enviarLoteAPIBanparaService",
                                            active: true,
                                        ]).save(flush: true)

            if (! DailySchedule.findWhere(processing: envioLote))
                new DailySchedule(processing: envioLote, hour: 10, minute: 0, executionFrequency: ExecutionFrequency.DAILY).save(flush: true)
        }

        if (grailsApplication.config.projeto.projectId == "smartvale") {
            //Processing.findOrCreateWhere([name: "Faturamento de Ordens de Servi??os - Omie API", order: 10 as byte, service: "faturamentoOrdemServiceOmieService", active: true, batch: batch]).save(flush: true)
        }

        Processing.findOrCreateWhere([name: "Rec??lculo Saldos de Clientes", order: 1 as byte,
                                      service: "recalculoSaldosConveniosService", active: true])
                                      .save(flush: true)

        Processing.findOrCreateWhere([name: "Gera????o Cart??es Provis??rios", order: 2 as byte,
                                      service: "processadorSolicitacaoCartaoProvisorioService", active: true])
                                      .save(flush: true)

        Processing.findOrCreateWhere([name: "Reset/Envio Senhas de Cart??es Provis??rios", order: 3 as byte,
                                      service: "resetEnvioSenhaCartaoProvisorioService", active: true])
                                      .save(flush: true)

    }

    private def criarMarcasVeiculos() {

        MarcaVeiculo.findOrCreateWhere([nome: "CHEVROLET", abreviacao: 'CHEVY']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "VOLKSWAGEM", abreviacao: 'VW']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "FIAT", abreviacao: 'FIAT']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "FORD", abreviacao: 'FORD']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "RENAULT", abreviacao: 'REN']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "TOYOTA", abreviacao: 'TYO']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "HONDA", abreviacao: 'HON']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "HYUNDAI", abreviacao: 'HYU']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "NISSAN", abreviacao: 'NIS']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "PEUGEOT", abreviacao: 'PGT']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "CITRO??N", abreviacao: 'CIT']).save(flush: true)
    }
    
    private def criarMotivosNegacao() {

        MotivoNegacao.findOrCreateWhere([codigo: "01", descricao: "Transa????o inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "02", descricao: "Estabelecimento inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "03", descricao: "Produto inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "04", descricao: "Produto n??o vinculado ao EC"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "05", descricao: "Funcion??rio inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "06", descricao: "Ve??culo inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "07", descricao: "Cart??o inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "08", descricao: "Portador inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "09", descricao: "Ve??culo n??o vinculado ao Funcion??rio"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "11", descricao: "Estabelecimento bloqueado"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "12", descricao: "Pre??o inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "13", descricao: "Cart??o n??o ativo"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "14", descricao: "Combust??vel inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "15", descricao: "Quantidade combust??vel inv??lida"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "17", descricao: "Funcion??rio n??o ativo"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "18", descricao: "Equipamento inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "19", descricao: "Equipamento n??o vinculado ao Funcion??rio"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "20", descricao: "Produtos inv??lidos"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "21", descricao: "Intervalo de Tempo inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "22", descricao: "Intervalo Percorrido inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "23", descricao: "Hor??rio inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "24", descricao: "Qtde litros abastecidos inv??lida"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "25", descricao: "Dia semana inv??lido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "26", descricao: "EC n??o habilitado ?? Empresa Cliente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "51", descricao: "Saldo indispon??vel"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "55", descricao: "Senha inv??lida"]).save(flush: true)
    }

    private void setDataFeriado(Holiday feriado, ano) {
        def feriadoData = new Date()
        feriadoData.set([dayOfMonth: feriado.day, month: feriado.month, year: ano])
        NationalHolidayDate.findOrCreateWhere(holiday: feriado, date: feriadoData.clearTime()).save(flush: true)
    }

    private void criarFeriadosNacionais() {
        NationalHoliday anoNovo = NationalHoliday.findOrCreateWhere(day: 1, month: 0, description: "Confraterniza????o Universal").save(flush: true)
        NationalHoliday independencia = NationalHoliday.findOrCreateWhere(day: 7, month: 6, description: "Independ??ncia do Brasil").save(flush: true)
        NationalHoliday aparecida = NationalHoliday.findOrCreateWhere(day: 12, month: 9, description: "Nossa Senhora Aparecida").save(flush: true)
        NationalHoliday finados = NationalHoliday.findOrCreateWhere(day: 2, month: 10, description: "Finados").save(flush: true)
        NationalHoliday republica = NationalHoliday.findOrCreateWhere(day: 15, month: 10, description: "Proclama????o da Rep??blica").save(flush: true)
        NationalHoliday natal = NationalHoliday.findOrCreateWhere(day: 25, month: 11, description: "Natal").save(flush: true)
        NationalHoliday carnaval = NationalHoliday.findOrCreateWhere(description: "Carnaval").save(flush: true)

        def hoje = new Date()
        def anoAtual = hoje[Calendar.YEAR]

        setDataFeriado(anoNovo, anoAtual)
        setDataFeriado(independencia, anoAtual)
        setDataFeriado(aparecida, anoAtual)
        setDataFeriado(finados, anoAtual)
        setDataFeriado(republica, anoAtual)
        setDataFeriado(natal, anoAtual)


        def carnavalData = new Date()
        carnavalData.set([dayOfMonth: 28, month: 1, year: anoAtual])
        NationalHolidayDate.findOrCreateWhere(holiday: carnaval, date: carnavalData.clearTime()).save(flush: true)
        carnavalData.set([dayOfMonth: 01, month: 2, year: anoAtual])
        NationalHolidayDate.findOrCreateWhere(holiday: carnaval, date: carnavalData.clearTime()).save(flush: true)



        //Feriados Bel??m
        Cidade belem = Cidade.findWhere(nome: "BEL??M", estado: Estado.findWhere(uf: 'PA'))
        MunicipalHoliday recirio = MunicipalHoliday.findOrCreateWhere(month: 9, description: 'Rec??rio', city: belem).save(flush: true)
        def recirioData = new Date()
        recirioData.set([dayOfMonth: 25, month: recirio.month, year: 2021])
        MunicipalHolidayDate.findOrCreateWhere(holiday: recirio, date: recirioData.clearTime()).save(flush: true)
    }
}
