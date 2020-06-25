package fleet.app

import com.fourLions.processingControl.BatchProcessing
import com.fourLions.processingControl.DailySchedule
import com.fourLions.processingControl.ExecutionFrequency
import com.fourLions.processingControl.Processing
import com.sysdata.gestaofrota.*
import grails.core.GrailsApplication

class BootStrap {

    GrailsApplication grailsApplication

    def init = { servletContext ->

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
        criarCidades()
        criarBancos()
        criarProcessamentos()
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
        if (!User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72").save()
            UserRole.create startUser, startRole
            UserRole.withSession {
                it.flush()
                it.clear()
            }
            log.info "Start User criado"
        }
    }

    def criarCidades() {
        Estado para = Estado.findOrCreateWhere([nome: "Pará", uf: "PA"]).save(flush: true)
        Estado bahia = Estado.findOrCreateWhere([nome: "Bahia", uf: "BA"]).save(flush: true)
        Cidade.findOrCreateWhere([nome: "Belém", estado: para]).save(flush: true)
        Cidade.findOrCreateWhere([nome: "Salvador", estado: bahia]).save(flush: true)
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
    }

}