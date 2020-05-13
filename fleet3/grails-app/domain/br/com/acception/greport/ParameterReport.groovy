package br.com.acception.greport

import com.sysdata.gestaofrota.Role

class ParameterReport {

    String name
    String label
    DataType dataType
    MarkupType markupType
    Integer order
    boolean mandatory

    static belongsTo = [report: Report]
    static hasMany = [roles: Role]

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'paramreport_seq']
        order column: 'ord'
    }
}
