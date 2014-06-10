package br.com.acception.greport

class Report {
	
	String name
	String query
	String countQuery
	QueryType queryType
	
	static hasMany=[parameters:ParameterReport,fields:FieldReport]

    static constraints = {
		countQuery nullable:true
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'report_seq']
		query type:"text"
		countQuery type:"text"
	}
}
