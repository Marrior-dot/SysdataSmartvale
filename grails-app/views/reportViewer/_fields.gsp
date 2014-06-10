<%	
	def currGroup=[:]

	def fieldsGroup=reportInstance.fields.findAll{it.groupBy}
	
	fieldsGroup.each{fieldInstance->
		currGroup[fieldInstance.name]=''
	}
 %>


<table>

	<% 
		if(currGroup.isEmpty()){
	%>
		<thead>
			<tr>
				<g:each in="${reportInstance.fields.sort{it.order}}" var="fieldInstance">
					<th class='sortable'>${fieldInstance.label}</th>
				</g:each>
			</tr>
		</thead>
		
		<tbody>
		
			<g:each in="${rows}" var="rowInstance" status="i">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:each in="${reportInstance.fields.sort{it.order}}" var="fieldInstance">
						<td>${rowInstance[fieldInstance.name]}</td>
					</g:each>
				</tr>
			</g:each>
		</tbody>

	<% } else {
		
			
			
			rows.eachWithIndex{row,i->
			
				def nobreak=true
			
				currGroup.each{k,v->
					nobreak=nobreak && (row[k]==v)
				}

				if(!nobreak){
					fieldsGroup.each{fld->
						currGroup[fld.name]=row[fld.name]
					}
			
	%>
		
			<thead>
			
				<tr>
					<g:each in="${fieldsGroup}" var="grpField">
						<th class='sortable' colspan='2'>${grpField.label}: ${currGroup[grpField.name]}</th>
					</g:each>
				</tr>
	
				<tr>
					<g:each in="${reportInstance.fields.findAll{!it.groupBy}.sort{it.order}}" var="fieldInstance">
						<th class='sortable'>${fieldInstance.label}</th>
					</g:each>
				</tr>
			</thead>
		
			<tbody>
		
	<% 			}	%>

				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:each in="${reportInstance.fields.findAll{!it.groupBy}.sort{it.order}}" var="fieldInstance">
						<td>${row[fieldInstance.name]}</td>
					</g:each>
				</tr>

	<%		} %>
			</tbody>
	<%	} 	%>
	
	<g:if test="${rowTotal}">
	
		<tfoot>
			<g:each in="${rowTotal}" var="total">
				<tr>
					<td>Total ${total.key} ${total.value}</td>
				</tr>
			</g:each>
		</tfoot>
	</g:if>
	
</table>
