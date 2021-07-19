package br.com.acception.greport

enum QueryType {

	SQL("SQL"),
	HQL("HQL"),
	CRITERIA("Criteria")
	
	String name
	
	QueryType(name){
		this.name=name
	}
}
