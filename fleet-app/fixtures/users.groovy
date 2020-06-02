import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserRole

fixture {

    println("CRIANDO ROLES...")
    roleProc(Role, authority: "ROLE_PROC", owner:"Processadora" )
    roleAdmin(Role, authority: "ROLE_ADMIN",owner: "Administradora")
    roleRh(Role, authority: "ROLE_RH",owner:"RH")
    roleEstab(Role, authority: "ROLE_ESTAB",owner:"Estabelecimento")
    roleLog(Role, authority: "ROLE_LOG",owner:"Processadora")
    roleHelp(Role, authority: "ROLE_HELP",owner:"Processadora")

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