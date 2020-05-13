package br.com.acception.greport
import com.sysdata.gestaofrota.Role

class Report {
    String name
    String query
    String countQuery
    QueryType queryType

    static hasMany = [parameters: ParameterReport, fields: FieldReport, roles: Role]

    static constraints = {
        countQuery nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'report_seq']
        query type: "text"
        countQuery type: "text"
    }
}
