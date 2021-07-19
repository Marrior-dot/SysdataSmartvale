package com.sysdata.gestaofrota.ca

class NsuTermFile {

	private static final int START_NSU=1
	
	private static def updateNsuFile(newNsu){
		
		def outFile=new File(".nsuterm.dat")
		outFile.withWriter{w->
			w<<String.format("%06d",newNsu)
		}
	}
	
	def synchronized static nextNsu(){
		def nsu=START_NSU
		def inFile=new File(".nsuterm.dat")
		if(inFile.exists()){
			inFile.withReader{r->
				nsu=r.readLine() as int
				updateNsuFile(++nsu)
			}
		}else updateNsuFile(START_NSU)
		nsu
	}
}
