package com.sysdata.gestaofrota

class CategoriaFuncionarioService {

    def filter(def params) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def criteria = {
            if (params?.rhId && params.rhId.toString().length() > 0) {
                rh {
                    idEq(params.long('rhId'))
                }
            }

            if (params?.searchNome && params.searchNome.length() > 0) {
                ilike('nome', "%${params?.searchNome}%")
            }

            order("id")
        }

        def criteriaCount = {
            if (params?.rhId && params.rhId.toString().length() > 0) {
                rh {
                    idEq(params.long('rhId'))
                }
            }

            if (params?.searchNome && params.searchNome.length() > 0) {
                ilike('nome', "%${params?.searchNome}%")
            }
        }

        def categoriaInstanceList = CategoriaFuncionario.createCriteria().list(params, criteria)
        def categoriaInstanceCount = CategoriaFuncionario.createCriteria().count(criteriaCount)

        [categoriaInstanceList: categoriaInstanceList, categoriaInstanceCount: categoriaInstanceCount]
    }

    def save(Rh rh, String nome, Double valorCarga) {
        CategoriaFuncionario categoria = new CategoriaFuncionario(nome: nome, valorCarga: valorCarga)
        rh.addToCategoriasFuncionario(categoria)
        //categoria.save(failOnError: true)

        def ret = [:]

        if (!rh.save(flush: true)) {
            log.error "Erro ao Salvar RH:"
            rh.errors.allErrors.each {
                log.error it.toString()
            }

            ret.success = false
            ret.message = "Erro ao Salvar dados de Cliente"
        } else
            ret.success = true

        return ret

    }

    void delete(CategoriaFuncionario categoriaFuncionario) {
        Rh rh = categoriaFuncionario.rh
        rh.removeFromCategoriasFuncionario(categoriaFuncionario)
        categoriaFuncionario.delete()
        rh.save()
    }

    void update(CategoriaFuncionario categoria, String nome, Double valor) {
        if (categoria == null) return
        categoria.nome = nome
        categoria.valorCarga = valor
        categoria.save(flush: true)
    }

    def listCategoriasByUnidade(Unidade unidade) {

        def categList = []

        if (unidade.rh.vinculoCartao == TipoVinculoCartao.FUNCIONARIO) {
            categList = Funcionario.withCriteria {
                                        projections {
                                            distinct "categoria"
                                        }
                                        eq("unidade", unidade)
            }

        } else if (unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA) {
            categList = MaquinaMotorizada.withCriteria {
                                            projections {
                                                distinct "categoria"
                                            }
                                            eq("unidade", unidade)
                                        }

        }

        if (categList)
            return categList.collect { [ id: it.id, nome: it.nome, valorCarga: Util.formatCurrency(it.valorCarga) ] }
        else
            return categList

    }

}
