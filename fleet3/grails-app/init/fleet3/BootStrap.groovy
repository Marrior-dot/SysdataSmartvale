package fleet3

import com.sysdata.gestaofrota.Administradora
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
        criarProcessadora()
        criarAdministradora()
        criarAutenticacaoInicial()
        criarCidades()
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

}
