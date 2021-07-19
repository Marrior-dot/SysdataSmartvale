package br.com.acception.greport

enum DataType {

	STRING("String"),
	INTEGER("Integer"),
	FLOAT("Float"),
	LONG("Long"),
	DOUBLE("Double"),
	DATE("Date"),
	TIMESTAMP("TimeStamp")
	
	String name
	
	DataType(name){
		this.name=name
	}
	
}
