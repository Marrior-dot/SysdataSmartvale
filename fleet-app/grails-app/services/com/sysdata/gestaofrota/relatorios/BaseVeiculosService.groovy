import com.sysdata.gestaofrota.Veiculo



class BaseVeiculosService {

    def list(params) {

        def baseVeiculoList = Veiculo.executeQuery("""
        select

          v.placa,
          v.marca,
          v.modelo,
          v.unidade.rh,
          v.unidade.nome,
          v.capacidadeTanque,
          v.chassi,
          v.categoria.nome,
          v.hodometro,
          v.validadeExtintor,
          v.funcionarios.size


            from Veiculo V



        """, [
        ], [max: params.max ? params.max as int : 10, offset: params.offset ? params.offset as int : 0])

        return baseVeiculoList
    }

    def count() {
        def baseVeiculoCount = Veiculo.executeQuery("""
        select
            count(v.id)
        from

            Veiculo v



        """, [

        ])

        return baseVeiculoCount

    }

}