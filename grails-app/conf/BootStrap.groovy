import br.com.acception.greport.Report
import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Banco
import com.sysdata.gestaofrota.Cidade
import com.sysdata.gestaofrota.Estado
import com.sysdata.gestaofrota.FuncionarioService
import com.sysdata.gestaofrota.MarcaVeiculo
import com.sysdata.gestaofrota.MotivoNegacao
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.Unidade
import grails.converters.JSON

class BootStrap {
    def fixtureLoader

    def init = { servletContext ->
        loadFixtures()
        loadObjectMarshallers()
    }

    def destroy = {}


    private void loadFixtures() {
        if (Administradora.count() == 0 || Processadora.count() == 0)
            fixtureLoader.load("processadora")

        if (Role.count() == 0)
            fixtureLoader.load("users")
        if (Banco.count() == 0)
            fixtureLoader.load("bancos")
        if (ParametroSistema.count == 0)
            fixtureLoader.load("parametros")

        if (Estado.count() == 0)
            fixtureLoader.load("all_estados")
        if (Cidade.count() == 0) {
            fixtureLoader.load("cidades_ab")
            fixtureLoader.load("cidades_cde")
            fixtureLoader.load("cidades_gm")
            fixtureLoader.load("cidades_p")
            fixtureLoader.load("cidades_r")
            fixtureLoader.load("cidades_st")
        }

        if (MarcaVeiculo.count == 0) {
            fixtureLoader.load("marcas_veiculo")
        }

        if (Report.count() == 0) {
            fixtureLoader.load("reports")
        }

        if (MotivoNegacao.count() == 0) {
            fixtureLoader.load("motivos_negacao")
        }
    }

    private void loadObjectMarshallers() {
        JSON.registerObjectMarshaller(Unidade) {
            [
                    id    : it.id,
                    nome  : it.nome,
                    codigo: it.codigo
            ]
        }
    }

}
