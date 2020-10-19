package fleet.app

import com.fourLions.processingControl.BatchProcessing
import com.fourLions.processingControl.DailySchedule
import com.fourLions.processingControl.ExecutionFrequency
import com.fourLions.processingControl.Processing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.camel.FleetRoutesService
import com.sysdata.xfiles.FileProcessor
import grails.core.GrailsApplication
import grails.util.Holders

class BootStrap {

    GrailsApplication grailsApplication

    FleetRoutesService fleetRoutesService

    def init = { servletContext ->


        if (grailsApplication.config.projeto.projectId == "banpara" && grailsApplication.config.projeto.reembolso.banpara.api.jksFile) {
            System.setProperty("javax.net.ssl.trustStore", grailsApplication.config.projeto.reembolso.banpara.api.jksFile)
            System.setProperty("javax.net.ssl.trustStorePassword", "sysdata")

        }

        FileProcessor.init()

        /* Adição do método ** round ** a classe BigDecimal: arredondamento para baixo. P.ex: 1.5 -> 1.0 (padrão -> halfUp = false) */
        BigDecimal.metaClass.round = { precision, halfUp = true ->
            delegate.setScale(precision, halfUp ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN)
        }

        /* Adição do método ** incMonth ** a classe Date: adiciona meses a data em questão */
        Date.metaClass.incMonth = { m ->
            use(groovy.time.TimeCategory) {
                return delegate + m.month
            }
        }

        criarProcessadora()
        criarAdministradora()
        criarAutenticacaoInicial()
        criarEstadosCapitais()
        criarBancos()
        criarProcessamentos()
        criarMarcasVeiculos()

        fleetRoutesService.init()
    }

    def destroy = {
    }

    def criarProcessadora() {
        Processadora.findOrCreateWhere(nome: "Sysdata Tecnologia").save(flush: true)
        assert Processadora.count() == 1
    }

    def criarAdministradora() {
        def adm = grailsApplication.config.projeto.administradora
        Administradora.findOrCreateWhere([bin: adm.bin, nome: adm.nome]).save(flush: true)
        assert Administradora.count() == 1
        ParametroSistema.findOrCreateWhere([nome: ParametroSistema.ANOS_VALIDADE_CARTAO, valor: adm.anosValidadeCartao.toString(), escopo: EscopoParametro.ADMINISTRADORA, tipoDado: TipoDado.INTEGER]).save(flush: true)
    }


    def criarAutenticacaoInicial() {
        Role startRole = Role.findOrCreateWhere(authority: "ROLE_PROC").save()
        User startUser
        if (! User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72", owner: Processadora.first())
            startUser.save()
            UserRole.create startUser, startRole
            log.info "Start User criado"
        }
    }

    def criarEstadosCapitais() {
        Estado acre = Estado.findOrCreateWhere([uf: "AC", nome: "ACRE"]).save()
        Estado alagoas = Estado.findOrCreateWhere([uf: "AL", nome: "ALAGOAS"]).save()
        Estado amazonas = Estado.findOrCreateWhere([uf: "AM", nome: "AMAZONAS"]).save()
        Estado amapa = Estado.findOrCreateWhere([uf: "AP", nome: "AMAPÁ"]).save()
        Estado bahia = Estado.findOrCreateWhere([uf: "BA", nome: "BAHIA"]).save()
        Estado ceara = Estado.findOrCreateWhere([uf: "CE", nome: "CEARÁ"]).save()
        Estado df = Estado.findOrCreateWhere([uf: "DF", nome: "DISTRITO FEDERAL"]).save()
        Estado espSanto = Estado.findOrCreateWhere([uf: "ES", nome: "ESPÍRITO SANTO"]).save()
        Estado goias = Estado.findOrCreateWhere([uf: "GO", nome: "GOIÁS"]).save()
        Estado maranhao = Estado.findOrCreateWhere([uf: "MA", nome: "MARANHÃO"]).save()
        Estado minas = Estado.findOrCreateWhere([uf: "MG", nome: "MINAS GERAIS"]).save()
        Estado matoGrossoSul = Estado.findOrCreateWhere([uf: "MS", nome: "MATO GROSSO DO SUL"]).save()
        Estado matoGrosso = Estado.findOrCreateWhere([uf: "MT", nome: "MATO GROSSO"]).save()
        Estado para = Estado.findOrCreateWhere([uf: "PA", nome: "PARÁ"]).save()
        Estado paraiba = Estado.findOrCreateWhere([uf: "PB", nome: "PARAÍBA"]).save()
        Estado pernambuco = Estado.findOrCreateWhere([uf: "PE", nome: "PERNAMBUCO"]).save()
        Estado piaui = Estado.findOrCreateWhere([uf: "PI", nome: "PIAUÍ"]).save()
        Estado parana = Estado.findOrCreateWhere([uf: "PR", nome: "PARANÁ"]).save()
        Estado rio = Estado.findOrCreateWhere([uf: "RJ", nome: "RIO DE JANEIRO"]).save()
        Estado rioGrandeNorte = Estado.findOrCreateWhere([uf: "RN", nome: "RIO GRANDE DO NORTE"]).save()
        Estado rondonia = Estado.findOrCreateWhere([uf: "RO", nome: "RONDÔNIA"]).save()
        Estado rioGrandeSul = Estado.findOrCreateWhere([uf: "RS", nome: "RIO GRANDE DO SUL"]).save()
        Estado roraima = Estado.findOrCreateWhere([uf: "RR", nome: "RORAIMA"]).save()
        Estado santaCatarina = Estado.findOrCreateWhere([uf: "SC", nome: "SANTA CATARINA"]).save()
        Estado sergipe = Estado.findOrCreateWhere([uf: "SE", nome: "SERGIPE"]).save()
        Estado saoPaulo = Estado.findOrCreateWhere([uf: "SP", nome: "SÃO PAULO"]).save()
        Estado tocantins = Estado.findOrCreateWhere([uf: "TO", nome: "TOCANTINS"]).save()

        Cidade.findOrCreateWhere([nome: "RIO BRANCO", estado: acre]).save()
        Cidade.findOrCreateWhere([nome: "MACEIÓ", estado: alagoas]).save()
        Cidade.findOrCreateWhere([nome: "MANAUS", estado: amazonas]).save()
        Cidade.findOrCreateWhere([nome: "MACAPÁ", estado: amapa]).save()
        Cidade.findOrCreateWhere([nome: "SALVADOR", estado: bahia]).save()
        Cidade.findOrCreateWhere([nome: "FORTALEZA", estado: ceara]).save()
        Cidade.findOrCreateWhere([nome: "BRASÍLIA", estado: df]).save()
        Cidade.findOrCreateWhere([nome: "VITÓRIA", estado: espSanto]).save()
        Cidade.findOrCreateWhere([nome: "GOIÂNIA", estado: goias]).save()
        Cidade.findOrCreateWhere([nome: "SÃO LUÍS", estado: maranhao]).save()
        Cidade.findOrCreateWhere([nome: "BELO HORIZONTE", estado: minas]).save()
        Cidade.findOrCreateWhere([nome: "CAMPO GRANDE", estado: matoGrossoSul]).save()
        Cidade.findOrCreateWhere([nome: "CUIABÁ", estado: matoGrosso]).save()
        Cidade.findOrCreateWhere([nome: "BELÉM", estado: para]).save()
        Cidade.findOrCreateWhere([nome: "JOÃO PESSOA", estado: paraiba]).save()
        Cidade.findOrCreateWhere([nome: "RECIFE", estado: pernambuco]).save()
        Cidade.findOrCreateWhere([nome: "TERESINA", estado: piaui]).save()
        Cidade.findOrCreateWhere([nome: "CURITIBA", estado: parana]).save()
        Cidade.findOrCreateWhere([nome: "RIO DE JANEIRO", estado: rio]).save()
        Cidade.findOrCreateWhere([nome: "NATAL", estado: rioGrandeNorte]).save()
        Cidade.findOrCreateWhere([nome: "PORTO VELHO", estado: rondonia]).save()
        Cidade.findOrCreateWhere([nome: "PORTO ALEGRE", estado: rioGrandeSul]).save()
        Cidade.findOrCreateWhere([nome: "BOA VISTA", estado: roraima]).save()
        Cidade.findOrCreateWhere([nome: "FLORIANÓPOLIS", estado: santaCatarina]).save()
        Cidade.findOrCreateWhere([nome: "ARACAJÚ", estado: sergipe]).save()
        Cidade.findOrCreateWhere([nome: "SÃO PAULO", estado: saoPaulo]).save()
        Cidade.findOrCreateWhere([nome: "PALMAS", estado: tocantins]).save()
    }

    def criarBancos() {
        Banco.findOrCreateWhere([codigo: "1", nome: "BANCO DO BRASIL S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "104", nome: "CAIXA ECONOMICA FEDERAL"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "33", nome: "BANCO SANTANDER BRASIL S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "237", nome: "BANCO BRADESCO S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "341", nome: "BANCO ITAU S.A."]).save(flush: true)
    }

    def criarProcessamentos() {

        BatchProcessing batch = BatchProcessing.findByName("Processamento Diário")
        if (! batch) {
            batch = new BatchProcessing([name: "Processamento Diário",
                                 active: true,
                                 executionSchedule: new DailySchedule(hour: 0, minute: 10, executionFrequency: ExecutionFrequency.DAILY)])
            batch.save(flush: true)
        }

        Processing.findOrCreateWhere([name: "Carga de Pedidos", order: 1 as byte, service: "cargaPedidoService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Agendamento de Transações", order: 2 as byte, service: "agendamentoTransacaoService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Faturamento Pedidos de Carga", order: 3 as byte, service: "faturamentoCargaPedidoService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Faturamento Portador", order: 4 as byte, service: "faturamentoService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Geração Remessa Cobrança Banco do Brasil", order: 5 as byte, service: "geradorRemessaBancoBrasilService", active: true, batch: batch]).save(flush: true)

        if (grailsApplication.config.projeto.projectId == "bahiavale")
            Processing.findOrCreateWhere([name: "Geração Arquivo NFe - RPS Barueri", order: 6 as byte, service: "geracaoArquivoRPSBarueriService", active: true, batch: batch]).save(flush: true)

        Processing.findOrCreateWhere([name: "Atualização de Saldos", order: 7 as byte, service: "atualizacaoSaldoService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Faturamento Estabelecimento", order: 8 as byte, service: "corteReembolsoEstabsService", active: true, batch: batch]).save(flush: true)
        Processing.findOrCreateWhere([name: "Fechamento de Lote Pagamento", order: 9 as byte, service: "fechamentoLotePagamentoService", active: true, batch: batch]).save(flush: true)

        if (grailsApplication.config.projeto.projectId == "banpara")
            Processing.findOrCreateWhere([name: "API Reembolso Banpará", order: 10 as byte, service: "banparaReembolsoAPIService", active: true, batch: batch]).save(flush: true)


    }

    def criarMarcasVeiculos() {

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
        MarcaVeiculo.findOrCreateWhere([nome: "CITROËN", abreviacao: 'CIT']).save(flush: true)
    }
}