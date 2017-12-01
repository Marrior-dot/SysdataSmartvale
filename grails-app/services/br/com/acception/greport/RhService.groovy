package br.com.acception.greport

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Status
import com.sysdata.gestaofrota.TipoVinculoCartao

class RhService {
    def participanteService

    Rh save(Rh rh) {
        rh.codigo = getProximoCodigoFormatado()

        if (rh.validate()) rh.save()
        else throw new RuntimeException(rh.showErrors())

        participanteService.saveCidade(rh.endereco)

        rh
    }

    void inativar(Rh rhInstance) {
        try {
            rhInstance.status = Status.INATIVO
            rhInstance.empresas.each { empresa ->
                empresa.status = Status.INATIVO
                empresa.estabelecimentos.each { estabelecimento ->
                    estabelecimento.status = Status.INATIVO
                    estabelecimento.save()
                }
                empresa.save()
            }

            rhInstance.unidades.each { uni ->
                uni.status = Status.INATIVO
                uni.funcionarios.each { fun ->
                    fun.status = Status.INATIVO
                    fun.save()
                }
                uni.save()
            }

            rhInstance.save()
        }
        catch (Exception e) {
            println(e.message)
            e.printStackTrace()
        }

    }

    String getProximoCodigoFormatado() {
        Long ultimoCodigo = Rh.list().max { it.codigo as Long }?.codigo as Long ?: 1
        return String.format("%04d", ultimoCodigo + 1);
    }
}
