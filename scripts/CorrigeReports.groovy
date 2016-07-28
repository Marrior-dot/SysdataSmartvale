import br.com.acception.greport.Report
import com.sysdata.gestaofrota.Role

def roles = Role.list()

Report.list().each { report ->
    report.roles.clear()
    roles.each { role ->
        report.addToRoles(role)
    }

    report.parameters.each { parameter ->
        parameter.roles.clear()
        report.roles.each { role ->
            parameter.addToRoles(role)
        }
    }
}