package com.sysdata.gestaofrota

class BaseOwnerController {


	def springSecurityService
	
	def withSecurity={cls->
		def userInstance=springSecurityService.currentUser
		
		def ownerList=[]
		
		if(userInstance.owner instanceof Rh){
			ownerList<<userInstance.owner.id
		}else if(userInstance.owner instanceof Administradora){
			ownerList<<userInstance.owner.id
			ownerList+=Rh.all*.id
		}else{
			ownerList<<userInstance.owner.id
			ownerList+=Administradora.all*.id
			ownerList+=Rh.all*.id
		}
	
		cls.call(ownerList)
		
	}
	
	
	def listOwners(){
		def userInstance=springSecurityService.currentUser
		
		def ownerList=[]
		
		if(userInstance.owner instanceof Rh){
			ownerList<<userInstance.owner
		}else if(userInstance.owner instanceof Administradora){
			ownerList<<userInstance.owner
			ownerList+=Rh.all
		}else{
			ownerList+=Processadora.all
			ownerList+=Administradora.all
			ownerList+=Rh.all
		}
		
		ownerList
		
	}
}