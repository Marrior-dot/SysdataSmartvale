package com.sysdata.gestaofrota

import java.time.DayOfWeek

enum DiaSemana {
	
	DOMINGO("Domingo", DayOfWeek.SUNDAY),
	SEGUNDA("Segunda", DayOfWeek.MONDAY),
	TERCA("Terça", DayOfWeek.TUESDAY),
	QUARTA("Quarta", DayOfWeek.WEDNESDAY),
	QUINTA("Quinta", DayOfWeek.THURSDAY),
	SEXTA("Sexta", DayOfWeek.FRIDAY),
	SABADO("Sábado", DayOfWeek.SATURDAY)
	
	String nome
	DayOfWeek dayOfWeek
	
	DiaSemana(String nome, DayOfWeek diaSemana){
		this.nome = nome
		this.dayOfWeek = diaSemana
	}

}
