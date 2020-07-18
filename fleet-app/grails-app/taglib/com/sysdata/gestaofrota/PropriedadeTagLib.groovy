package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.propriedades.Prop
import com.sysdata.gestaofrota.propriedades.Configuration
import com.sysdata.gestaofrota.propriedades.PropertyGroup


class PropriedadeTagLib {

    static namespace = "dyn"

    def eval(String dsl) {
        def builder = new ObjectGraphBuilder()
        builder.classLoader = this.class.classLoader
        builder.classNameResolver = "com.sysdata.gestaofrota.propriedades"
        Binding binding = new Binding()
        binding.setVariable("builder", builder)
        return new GroovyShell(binding).evaluate(dsl)
    }

    Closure props = { attrs, body ->

        if (TipoParticipante.values().find { it.toString() == attrs.tipoParticipante} ) {
            List<ConfiguracaoPropriedade> configProps = ConfiguracaoPropriedade
                                                        .findAllByTipoParticipante(TipoParticipante.valueOf(attrs.tipoParticipante))
            configProps.each { cfg ->
                def root = eval(cfg.dsl)

                if (root instanceof Configuration) {

                    StringBuilder sb = new StringBuilder()

                    Configuration configuration = root as Configuration
                    configuration.propertyGroups.each { PropertyGroup grp ->

                        sb.append("""
<div class="panel panel-default">
    <div class="panel-heading">
        ${grp.name}
    </div>
    <div class="panel-body">
""")

                        def currRow
                        grp.props.sort{ it.order }.each { Prop prop ->

                            byte countPropsByRow = grp.countPropsInline(prop.row)
                            if (countPropsByRow > 4)
                                throw new RuntimeException("Quantidade de propriedades por linha não pode ser superior a 4")

                            byte size = 12 / countPropsByRow

                            if (!currRow || prop.row != currRow) {

                                // Fecha a div row que estava aberta pela linha anterior
                                if (currRow && prop.row != currRow)
                                    sb.append("</div>\n")

                                sb.append("<div class='row'>\n")
                            }

                            Propriedade propriedade = attrs.participante?.propriedades.find{ it.nome == prop.name}
                            sb.append("<div class='col-md-$size'>\n")
                            sb.append("<label for='propriedades[$prop.order].$prop.name'>$prop.label</label>\n")
                            sb.append("${g.hiddenField(name: "propriedades[$prop.order].id", value: propriedade?.id)}\n")
                            sb.append("${g.hiddenField(name: "propriedades[$prop.order].nome", value: "${prop.name}")}\n")
                            sb.append("${g.hiddenField(name: "propriedades[$prop.order].tipoDado", value: "${prop.dataType}")}\n")
                            sb.append("${g.textField(name: "propriedades[$prop.order].valor", class: 'form-control', value: "${propriedade ? propriedade.valorConvertido : ''}")}\n")
                            sb.append("</div>\n")

                            currRow = prop.row

                        }

                        // Fecha a div row da última linha aberta
                        sb.append("</div>\n")
                    }

                    sb.append("</div>\n")
                    sb.append("</div>\n")

                    out << sb.toString()
                }
            }
        } else
            out << "Tipo de Participante inválido ($attrs.tipoParticipante)"
    }
}