package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.springframework.http.HttpStatus


//@Secured(['IS_AUTHENTICATED_FULLY'])
class CategoriaFuncionarioController {

    def categoriaFuncionarioService

    static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

    def save() {
        String nome = params.nome
        Double valorCarga = Util.convertToDouble(params['valorCarga'] ?: "0")
        Rh rh = Rh.get(params.long('rh'))
        categoriaFuncionarioService.save(rh, nome, valorCarga)
        response.status = HttpStatus.OK.value()
        render(template: 'tabela', model: [rhInstance: Rh.get(params.long('rh'))])
    }

    def update() {
        CategoriaFuncionario categoriaFuncionario = CategoriaFuncionario.get(params.long('id'))
        categoriaFuncionarioService.update(categoriaFuncionario, params['nome'], Util.convertToDouble(params['valorCarga'] ?: "0"))
        response.status = HttpStatus.OK.value()
        render(template: 'tabela', model: [rhInstance: categoriaFuncionario.rh])
    }

    def delete() {
        CategoriaFuncionario categoriaFuncionario = CategoriaFuncionario.get(params.long('id'))
        Rh rh = categoriaFuncionario.rh
        categoriaFuncionarioService.delete(categoriaFuncionario)
        response.status = HttpStatus.OK.value()
        render(template: 'tabela', model: [rhInstance: rh])
    }

    def getAllByUnidade() {
        def unidId = params.unidId ? params.long('unidId') : 0 as long
        def categList = Funcionario.withCriteria {
                            projections {
                                distinct "categoria"
                            }
                            unidade {
                                idEq(unidId)
                            }
                        }
        if (categList)
            render categList.collect { [ id: it.id, nome: it.nome, valorCarga: Util.formatCurrency(it.valorCarga) ] } as JSON
        else
            render categList as JSON

    }
}
