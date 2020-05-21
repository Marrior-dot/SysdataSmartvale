package com.sysdata.gestaofrota

class CategoriaFuncionario {

    Date dateCreated
    String nome
    Double valorCarga

    static belongsTo = [rh: Rh]
    static transients = ['funcionarios']


    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'categfuncionario_seq']
    }
    static namedQueries = {
        porUnidade { Unidade unidade ->
            rh {
                idEq(unidade?.rh?.id)
            }
        }
    }

    List<Funcionario> getFuncionarios() {
        Funcionario.findAllByCategoria(this)
    }
}
