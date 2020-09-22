package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Funcionario

class BaseFuncionariosController {



    BaseFuncionariosService  baseFuncionariosService


    def index() {


        //testesService.list()

        if (params.f && params.f != 'html') {

        }

        [baseFuncionariosList: baseFuncionariosService.list(params), baseFuncionariosCount: baseFuncionariosService.count()]



    }

   /* def filtroUnid(params) {



            params.max = params.max ? params.max as int: 10

            def criteria = {

                if (params.unidade) {
                    portador {
                        unidade {
                            eq("id", params.unidade.toLong())
                        }
                    }
                }

            }

            def baseFuncionariosList = Funcionario.createCriteria().list(criteria)
            def baseFuncionariosCount = Funcionario.createCriteria().count(criteria)

            [baseFuncionariosList: baseFuncionariosList, baseFuncionariosCount: baseFuncionariosCount, params: params]

        }*/
    }

