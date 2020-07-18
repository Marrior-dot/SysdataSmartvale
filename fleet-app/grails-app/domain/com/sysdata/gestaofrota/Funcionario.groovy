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

    static belongsTo = [unidade: Unidade]
    static embedded = ['telefoneComercial']
    static hasMany = [veiculos: MaquinaFuncionario]
    static hasOne = [portador: PortadorFuncionario]

    static constraints = {
        cpf(blank: false, cpf: true)
        matricula(blank: false, validator: { val, obj ->
            Funcionario funcionario = Funcionario.findByUnidadeAndMatricula(obj.unidade, val)
            if (funcionario && funcionario.id != obj.id) return "funcionario.matricula.unica"
        })
        cnh(blank: false)
        portador nullable: true
        nomeEmbossing nullable: true
        telefoneComercial nullable: true
        categoria nullable: true
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


    public boolean isAtivoInPedido(PedidoCarga pedidoCarga) {
        if (!pedidoCarga) return false
        ItemPedido item = pedidoCarga.itens.find { it.participante.id == this.id && it.tipo == TipoItemPedido.CARGA }
        item?.ativo
    }

    public BigDecimal valorInPedido(PedidoCarga pedidoCarga, int decimalPlace = 2) {
        if (!pedidoCarga) return Util.toBigDecial(categoria.valorCarga, decimalPlace)
        ItemPedido item = pedidoCarga.itens.find { it.participante.id == this.id && it.tipo == TipoItemPedido.CARGA }
        Double valor = item?.valor ?: categoria.valorCarga
        Util.toBigDecial(valor, decimalPlace)
    }

    @Override
    String toString() {
        "$cpf - $nome"
    }
}
