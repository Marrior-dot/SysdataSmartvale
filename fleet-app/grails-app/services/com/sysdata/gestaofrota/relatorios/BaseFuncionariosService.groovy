package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Funcionario
import grails.core.GrailsApplication


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
        ], [max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0])

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

/*
        def teste() {

            /////

            def exportService

            GrailsApplication grailsApplication



            StringBuilder sb = new StringBuilder()

            sb.append("""
select
    f.matricula,
    f.nome,
    f.cpf,
    f.unidade.rh.nome,
    f.unidade.nome

from

    Funcionario f


""")


            if (params.unidade) {
                sb.append(" and f.unidade.id = ${params.unidade as long}")
            }


            sb.append("""
group by
    f.matricula,
    f.nome,
    f.cpf,
    f.unidade.rh.nome,
    f.unidade.nome

order by
   f.matricula
""")

            [consumoList: consumoList, consumoCount: consumoCount, params: params]

            ///////
*/
        }

    }
