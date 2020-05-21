package fleet3

import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Banco
import com.sysdata.gestaofrota.Cidade
import com.sysdata.gestaofrota.EscopoParametro
import com.sysdata.gestaofrota.Estado
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.TipoDado
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserRole
import grails.core.GrailsApplication
import grails.util.Holders

class BootStrap {

    GrailsApplication grailsApplication

    def init = { servletContext ->

        /* Adição do método ** round ** a classe BigDecimal: arredondamento para baixo. P.ex: 1.5 -> 1.0 (padrão -> halfUp = false) */
        BigDecimal.metaClass.round = { precision, halfUp = true ->
            delegate.setScale(precision, halfUp ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN)
        }

        criarProcessadora()
        criarAdministradora()
        criarAutenticacaoInicial()
        criarCidades()
        criarBancos()
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
        Role startRole = Role.findOrCreateWhere(authority: "ROLE_PROC", owner: Processadora.first()).save()
        User startUser
        if (! User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72", owner: Processadora.first()).save()
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
        Banco.findOrCreateWhere([codigo: "1", nome:"BANCO DO BRASIL S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "104", nome:"CAIXA ECONOMICA FEDERAL"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "33", nome:"BANCO SANTANDER BRASIL S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "237", nome:"BANCO BRADESCO S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "341", nome:"BANCO ITAU S.A."]).save(flush: true)
    }

}
