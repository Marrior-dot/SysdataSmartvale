package com.sysdata.gestaofrota.ca

class AuthAccessFile {

	private static final String TERMINAL="TERMINAL"
	private static final String AUTH_IP="AUTH_IP"
	private static final String AUTH_PORT="AUTH_PORT"
	private static final String TIMEOUT="TIMEOUT"
	
	private static Properties properties
	
	static{
		properties=new Properties()
		try {
			properties.load(new FileInputStream("auth.properties"))
		} catch (FileNotFoundException e) {
			
			/* Cria arquivo de acesso ao autorizador caso não exista */
			def outFile=new File("auth.properties")
			outFile.withWriter{w->
				w.writeLine "AUTH_IP=localhost"
				w.writeLine "AUTH_PORT=5000"
				w.writeLine "TERMINAL=12345678"
				w.writeLine "TIMEOUT=30000"
			}
			properties.load(new FileInputStream("auth.properties"))
		}
	}
	
	synchronized static String getTerminalNumber(){
		properties.getAt(TERMINAL)
	}
	
	synchronized static String getAuthIp(){
		properties.getAt(AUTH_IP)
	}
	
	synchronized static Integer getAuthPort(){
		Integer.parseInt(properties.getAt(AUTH_PORT))
	}
	
	synchronized static Integer getTimeout(){
		Integer.parseInt(properties.getAt(TIMEOUT))
	}

	
}
