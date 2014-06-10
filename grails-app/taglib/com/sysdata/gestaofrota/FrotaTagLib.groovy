package com.sysdata.gestaofrota

class FrotaTagLib {
	
	static namespace="fr"
	
	def checkDataTable={attrs,body->
	
		
		out<<"<input type='checkbox' name='${attrs.id}_selAll' >${attrs.txtSelAll}</input>"
		
		
		out<<gui.dataTable(id:attrs.id,
							controller:attrs.controller,
							action:attrs.action,
							params:attrs.params,
							columnDefs:attrs.columnDefs,
							sortedBy:attrs.sortedBy,
							rowsPerPage:attrs.rowsPerPage,
							paginatorConfig:attrs.paginatorConfig)
		
		
		out<<"""<script type="text/javascript">

					var countSel=0;
					var totalRec=0;

					function checkInput(check){
				
						countSel=check?(countSel+1):(countSel-1);
				
						\$("input[name='${attrs.id}_selAll']").attr("checked",(total==count));
					}



					function syncAll(check){
						\$.ajax({
							type:'POST',
							url:"${createLink(controller:attrs.controller,action:'syncAll')}",
							data:"chk="+check,
							success:function(o){
								if(o.type=="error")
									showError(o.message);
							},
							statusCode:{
								404:function(){
										showError("Falhar ao tentar conexão ao servidor");
									}
							}
						})
					}


					function syncOne(objId,chk){
						\$.ajax({
							type:"POST",
							url:"${createLink(controller:attrs.controller,action:'syncOne')}",
							data:"oid="+objId+"&chk="+chk,
							success:function(o){
								if(o.type=="error")
									showError(o.message);
							},
							statusCode:{
								404:function(){
									showError("Falha ao tentar conexão ao servidor");
								}
							}
						})
					}
		
					YAHOO.util.Event.onDOMReady(function(){


						GRAILSUI.${attrs.id}.subscribe("initEvent",function(args){
							totalRec=GRAILSUI.${attrs.id}.configs.paginator._configs.totalRecords.value;
						})

						
						GRAILSUI.${attrs.id}.subscribe("checkboxClickEvent",function(args){
							var elCheck=args.target;
							var newValue=elCheck.checked;
							var rec=this.getRecord(elCheck);
							var col=this.getColumn(elCheck);
							rec.setData(col.key,newValue);

							syncOne(rec.getData('id'),newValue);
							checkInput(newValue);	
						})
						

						\$("input[name='${attrs.id}_selAll']").click(function(){
								var check=\$(this).attr('checked')=='checked';
								var records=GRAILSUI.${attrs.id}.getRecordSet().getRecords();
								var i=0;
						
								while(i<records.length){
									if(records[i]!=undefined && records[i].getData('${attrs.keyCheck}')!=check)
										records[i].setData('${attrs.keyCheck}',check);
									i++;
								}
								GRAILSUI.${attrs.id}.render();

								syncAll(check);
								
								countSel=(check)?totalRec:0;
							})
					})


				
				</script>"""
		
	}

}
