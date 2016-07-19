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
}
