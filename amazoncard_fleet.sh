#!/bin/bash

# Gestao de Frota
# (C) Sysdata-GlobalCard 2012-2014
# Processamento Diario

date=`date +%Y-%m-%d`

base_dir_log="/home/globalcard"
proj_dir_log="fleet/log"
proj_name="amazon"
proj_file_log="${proj_name}_dailyprocessing_${date}.log"

log(){
	msg=$1
	
	## Cria se diretorio de log nao existe
		
	if [ ! -d $base_dir_log/$proj_dir_log ]; then
		cd $base_dir_log
		mkdir $proj_dir_log
	fi
	
	if [ ! -d $base_dir_log/$proj_dir_log/$proj_name ]; then
		cd $base_dir_log/$proj_dir_log
		mkdir $proj_name
	fi
	
	echo "$msg"
	echo "$msg" >> $base_dir_log/$proj_dir_log/$proj_name/$proj_file_log
}



main(){
	log "Iniciando Processamento Diário Gestao Frota - AmazonCard ..."
	
	cd ~/fleet/amazon
	
	source ~/.gvm/bin/gvm-init.sh
	
	gvm use grails 2.0.4
	
 	grails prod run-script scripts/DailyProcessing.groovy >> $base_dir_log/$proj_dir_log/$proj_name/$proj_file_log
 	
	log "Processamento Finalizado"

}


main
