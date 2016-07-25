package br.com.acception.greport

enum MarkupType {
	
	TEXT("Text"),
	SELECT("Select"),
	RADIO("Radio"),
	CHECK("Check"),
	DATE_PICKER("Date Picker"),
	READONLY("Readonly")
	
	String name
	
	MarkupType(name){
		this.name=name
	}
}
