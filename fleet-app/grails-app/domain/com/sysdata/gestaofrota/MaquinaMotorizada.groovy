package com.sysdata.gestaofrota

abstract class MaquinaMotorizada {
    Long capacidadeTanque
    TipoAbastecimento tipoAbastecimento
    Date dateCreated
    String complementoEmbossing
    Status status = Status.ATIVO
    CategoriaFuncionario categoria

    static auditable = true

    static hasOne = [portador: PortadorMaquina]
    static belongsTo = [unidade: Unidade]
    static hasMany = [funcionarios: MaquinaFuncionario]

    static constraints = {
        dateCreated nullable: true
        portador nullable: true
        complementoEmbossing nullable: true
        status nullable: true
        categoria nullable: true, validator: { val, obj ->
                                    if (obj.unidade.rh.vinculoCartao == TipoVinculoCartao.MAQUINA && !val)
                                        return "maquinaMotorizada.categoria.nula"
                                }
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'maquina_seq']
    }

    static transients = ['nomeEmbossing']

    static hibernateFilters = {
        maquinasPorRh(condition: 'unidade_id in (select u.id from Unidade u where u.rh_id = :rh_id)', types: 'long')
    }


    static namedQueries = {

        countMaquinasRh { Rh rh ->
            projections {
                rowCount("id")
            }
            unidade {
                eq("rh", rh)
            }
        }

        countMaquinasUnidade { Unidade unidade ->
            projections {
                rowCount("id")
            }
            eq("unidade", unidade)
        }
    }


    @Override
    String toString() {
        getNomeEmbossing()
    }

    abstract String getNomeEmbossing();
}
