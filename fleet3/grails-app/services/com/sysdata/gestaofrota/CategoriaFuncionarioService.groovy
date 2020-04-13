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

    void save(Rh rh, String nome, Double valorCarga) {
        CategoriaFuncionario categoria = new CategoriaFuncionario(nome: nome, valorCarga: valorCarga)
        rh.addToCategoriasFuncionario(categoria)
        categoria.save(failOnError: true)
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
        categoria.save()
    }
}
