package com.sysdata.gestaofrota

class ResetSenhaCartao {

    Date dateCreated
    User solicitante
    Date dataHoraSenhaEnviada
    StatusResetSenhaCartao status = StatusResetSenhaCartao.REGISTRADO

    static belongsTo = [cartao: Cartao]

    static hasMany = [funcionarios: Funcionario]

    static constraints = {
        solicitante nullable: true
        dataHoraSenhaEnviada nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'resetsenhacartao_seq']
    }

}
