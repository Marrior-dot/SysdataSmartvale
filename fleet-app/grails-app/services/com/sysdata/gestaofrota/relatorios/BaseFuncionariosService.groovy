package com.sysdata.gestaofrota.relatorios



import com.sysdata.gestaofrota.Funcionario


class BaseFuncionariosService {

    def list(params) {

        def baseFuncionariosList = Funcionario.executeQuery("""
        select
            f.matricula,
            f.nome,
            f.cpf,
            f.unidade.rh.nome,
            f.unidade.nome

            from Funcionario f

        """, [
        ], [max: params.max ? params.max as int: 10, offset: params.offset ? params.offset as int : 0])

        return baseFuncionariosList
    }

    def count() {
        def baseFuncionariosCount = Funcionario.executeQuery("""
        select
            count(f.id)
        from

            Funcionario f


        """, [

        ])

        return baseFuncionariosCount
    }

}
