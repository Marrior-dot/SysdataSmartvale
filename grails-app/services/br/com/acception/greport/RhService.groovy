package br.com.acception.greport

import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.TipoVinculoCartao

class RhService {
    def participanteService
    def messageSource

    Rh save(Rh rh) {
        rh.codigo = getProximoCodigoFormatado()

        if (!rh.validate()) {
            def list = rh.errors.allErrors.collect { messageSource?.getMessage(it, null) }
            throw new RuntimeException(list.join(System.getProperty("line.separator")))
        }
        participanteService.saveCidade(rh.endereco)
        rh.save()

        rh
    }

    String getProximoCodigoFormatado() {
        Long ultimoCodigo = Rh.list().max { it.codigo as Long }?.codigo as Long ?: 1
        return String.format("%04d", ultimoCodigo + 1);
    }
}
