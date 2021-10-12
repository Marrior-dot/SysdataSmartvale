package com.sysdata.gestaofrota

class Funcionario extends Participante {
    String cpf
    String matricula
    String rg
    String nomeEmbossing
    Date dataNascimento
    String cnh
    Date validadeCnh
    Telefone telefoneComercial
    CategoriaFuncionario categoria
    CategoriaCnh categoriaCnh
    boolean gestor
    Unidade unidade

    static belongsTo = [ResetSenhaCartao]

    static embedded = ['telefoneComercial']
    static hasMany = [veiculos: MaquinaFuncionario, resetsSenhaCartao: ResetSenhaCartao]
    static hasOne = [portador: PortadorFuncionario]

    static constraints = {
        cpf blank: false, cpf: true, validator: { val, obj ->

                                            if (! Util.validarCpf(val))
                                                return "funcionario.cpf.invalido"

                                            def mesmoCpf = Funcionario.withCriteria {
                                                                eq('cpf', val)
                                                                'in'('status', [Status.ATIVO, Status.BLOQUEADO])
                                                            }

                                            if (mesmoCpf && mesmoCpf[0].id != obj.id)
                                                return "funcionario.cpf.existente"
                                    }

        matricula(blank: false, validator: { val, obj ->
                                                Funcionario funcionario = Funcionario.findByUnidadeAndMatricula(obj.unidade, val)
                                                if (funcionario && funcionario.id != obj.id)
                                                    return "funcionario.matricula.unica"
                                            })
        cnh(blank: false)
        portador nullable: true
        nomeEmbossing nullable: true
        telefoneComercial nullable: true
        categoria nullable: true
    }

    static hibernateFilters = {
        funcionariosPorUnidade(condition: 'unidade_id in (select u.id from Unidade u where u.rh_id = :rh_id)', types: 'long')
    }

    static namedQueries = {

        countFuncionariosRh { Rh rh ->
            projections {
                rowCount("id")
            }
            unidade {
                eq("rh", rh)
            }
        }

        countFuncionariosUnidade { Unidade unidade ->
            projections {
                rowCount("id")
            }
            eq("unidade", unidade)
        }

        getFuncionariosAtivosUnidade { Unidade unidade ->
            eq("unidade", unidade)
        }
    }



    @Override
    String toString() {
        "$cpf - $nome"
    }
}
