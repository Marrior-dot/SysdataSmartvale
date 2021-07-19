<table>
	<thead>
		<tr>
			<th>Codigo</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${postoCombustivelInstance?.estabelecimentos}" var="e">
		<tr>
			<td>${e.codigo}</td>
		</tr>
	</g:each>
	</tbody>
</table>
