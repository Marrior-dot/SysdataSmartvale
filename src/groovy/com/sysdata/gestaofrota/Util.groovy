package com.sysdata.gestaofrota

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale;

import com.sysdata.gestaofrota.exception.InvalidCurrencyException

class Util {

	static final String ACTION_NEW="novo"
	static final String ACTION_VIEW="visualizando"
	static final String ACTION_EDIT="editando"
	static final String ACTION_FILTER="filtrando"
	
	static final Locale LOCAL=new Locale("pt","BR");
	
	static final int DIGITOS_SENHA=4;

	static def getStringMes(int mes){
		switch (mes){
			case 1: 'Jan'
				break
			case 2: 'Fev'
				break
			case 3: 'Mar'
				break
			case 4: 'Abr'
				break
			case 5: 'Mai'
				break
			case 6: 'Jun'
				break
			case 7: 'Jul'
				break
			case 8: 'Ago'
				break
			case 9: 'Set'
				break
			case 10: 'Out'
				break
			case 11: 'Nov'
				break
			case 12: 'Dez'
				break
		}
	}

	static def getIntegerMes(String mes){
		switch (mes){
			case 'Jan':1
				break
			case 'Fev':2
				break
			case 'Mar':3
				break
			case 'Abr':4
				break
			case 'Mai':5
				break
			case 'Jun':6
				break
			case 'Jul':7
				break
			case 'Ago':8
				break
			case 'Set':9
				break
			case 'Out':10
				break
			case 'Nov':11
				break
			case 'Dez':12
				break
		}
	}

	static def clearMask(masked){
		masked.replaceAll(/\W/,"")
	}
	
	
	static def decimalMode(curr){
		if(curr instanceof Double)
			curr*100 as long
		else
			throw new InvalidCurrencyException(message:"Valor $curr não é um Double")
	}
	
	
	static def isInteger(val){
		val==~/\d+/ 
	}
	
	static def isFloat(val){
		val==~/\d+,\d{2}/
	}
	
	static def convertToCurrency(value){
		if(value==~/\d+,\d{2}/){
			def convValue=value.replace(",",".")
			convValue=convValue.toDouble()
			convValue
		}else{
			throw new InvalidCurrencyException(message:"Valor $value não é um valor monetário válido!")
		}
	}
	
	static def formattedDate(date){
		def df=new SimpleDateFormat("dd/MM/yyyy")
		df.format(date)
	}
	
	static def formattedDateTime(date){
		def df=new SimpleDateFormat("dd/MM/yyyy HH:m:ss")
		df.format(date)
	}

	
	static def formatCurrency(curr){
		if(curr instanceof Double){
			def dfs=new DecimalFormatSymbols(LOCAL)
			dfs.decimalSeparator=","
			new DecimalFormat("#0.00",dfs).format(curr)
		}
	}
	
	static def roundCurrency(curr){
		if(curr instanceof Double){
			BigDecimal convert=new BigDecimal(curr)
			double arrend=convert.setScale(2, BigDecimal.ROUND_HALF_UP);
			arrend
		}
	}
	

	
}
