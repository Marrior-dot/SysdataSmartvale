import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserRole

fixture {
    println("CRIANDO ROLES...")
    roleProc(Role, authority: "ROLE_PROC")
    roleAdmin(Role, authority: "ROLE_ADMIN")
    roleRh(Role, authority: "ROLE_RH")
    roleEstab(Role, authority: "ROLE_ESTAB")
    roleLog(Role, authority: "ROLE_LOG")
    roleHelp(Role, authority: "ROLE_HELP")

    println("CRIANDO USUÁRIOS PADRÕES...")
    userSuporteAcception(User, owner: Processadora.findByNome("SYSDATA"), name: "Administrador Acception", username: 'suporte.acception', password: '@cceptionn0t3', email: "acception@acception.com.br",
            enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false)
    userLuiz(User, owner: Processadora.findByNome("SYSDATA"), name: "Luiz Leão", username: 'luiz.leao', password: '123', email: "luiz.leao@acception.com.br",
            enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false)
    userSuporteSysdata(User, owner: Processadora.findByNome("SYSDATA"), name: 'Sysdata', username: 'suporte.sysdata', password: 'jmml72', email: "sysdata@gmail.com",
            enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false)
}

post {
    UserRole.create userLuiz, roleProc
    UserRole.create userLuiz, roleAdmin

    UserRole.create userSuporteAcception, roleProc
    UserRole.create userSuporteAcception, roleAdmin

    UserRole.create userSuporteSysdata, roleProc
    UserRole.create userSuporteSysdata, roleAdmin
}