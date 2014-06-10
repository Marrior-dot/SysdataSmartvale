package br.com.acception.greport

class ParameterReport {

	String name
	String label
	DataType dataType
	MarkupType markupType
	Integer order
	boolean mandatory
	
	static belongsTo=[report:Report]
	
    static constraints = {
    }
	
	static mapping={
		id generator:'sequence',params:[sequence:'paramreport_seq']
		order column:'ord'
	}
}
