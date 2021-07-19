package com.sysdata.gestaofrota

class Estabelecimento extends Empresa {
    String codigo

    static belongsTo = [empresa: PostoCombustivel]

    static hasMany = [produtos: ProdutoEstabelecimento]

    static transients = ['descricaoResumida']

    static constraints = {
        codigo(unique: true)
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'estabelecimento_seq']
        produtos cascade: 'all-delete-orphan'
    }

    String getDescricaoResumida() {
        return (this.empresa.nomeFantasia.length() > 40) ? this.empresa.nomeFantasia[0..39] : this.empresa.nomeFantasia
    }
}
