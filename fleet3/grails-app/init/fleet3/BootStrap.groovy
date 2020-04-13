package fleet3

import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserRole

class BootStrap {

    def init = { servletContext ->
        criarAutenticacaoInicial()
    }
    def destroy = {
    }


    def criarAutenticacaoInicial() {
        Role startRole = Role.findOrCreateWhere(authority: "ROLE_PROC").save()
        User startUser
        if (! User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72").save()
            UserRole.create startUser, startRole
            UserRole.withSession {
                it.flush()
                it.clear()
            }
        }
        assert User.count() == 1
        assert Role.count() == 1
        assert UserRole.count() == 1

        log.info "Start User criado"
    }

}
