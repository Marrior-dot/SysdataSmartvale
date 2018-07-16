import br.com.acception.greport.Report
import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Arquivo
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

import java.text.SimpleDateFormat

class BootStrap {
    def fixtureLoader
    def grailsApplication
    def messageSource
    def grailsLinkGenerator

    def init = { servletContext ->
        loadFixtures()
        loadObjectMarshallers()
        bindFrontEndErrorsForDomainClasses()

        /* Adição do método ** round ** a classe BigDecimal: arredondamento para cima */
        BigDecimal.metaClass.round = { prec ->
            delegate.setScale(prec, BigDecimal.ROUND_HALF_UP)
        }

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

        JSON.registerObjectMarshaller(Arquivo) { Arquivo a ->
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            final String link = grailsLinkGenerator.link(controller: 'arquivo', action: 'downloadFile', id: a.id)

            [
                    id: a.id,
                 date: sdf.format(a.dateCreated),
                 tipo: a.tipo.name,
                 status: a.status.name,
                 nome: a.nome,
                 acao: "<a href='${link}'>Download</a>"
            ]
        }
    }



/**
 * Toda vez que um método validate() ou save() ocorrer em uma instância de dominio, basta usar o método
 * showErrors() para obter uma String contendo todos os errors dessa instância no formato legivel
 * para o usuário front end. Vá em grails-app/i18n/messages.properties ou messages_pt_PT.properties para
 * definir essas mensagens
 */
    void bindFrontEndErrorsForDomainClasses() {
        println("... Binding Front End Erros")
        String newLine = System.getProperty("line.separator")
        grailsApplication.domainClasses.each { domainClass ->
            if (domainClass.clazz.name.contains("com.sysdata.gestaofrota")) {
                domainClass.metaClass.showErrors = {
                    def list = delegate?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                    return list?.join(newLine)
                }
            }
        }
    }
}
