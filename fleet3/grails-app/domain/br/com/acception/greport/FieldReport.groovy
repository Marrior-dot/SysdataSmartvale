package br.com.acception.greport

class FieldReport {
	
	String name
	String label
	Integer order
	DataType dataType
	boolean totalizer
	boolean groupBy
	
	static belongsTo=[report:Report]
	
    static constraints = {
    }

	static mapping={
		id generator:'sequence',params:[sequence:'fieldreport_seq']
		order column:'ord'
	}	
	
}
