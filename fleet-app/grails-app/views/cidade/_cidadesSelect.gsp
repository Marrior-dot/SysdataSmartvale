<label for="cidades">Cidade</label>
<select name="cidades" class="form-control selectpicker enable">
    <g:each in="${cidadeInstanceList}" var="cid">
        <option value="${cid.id}">${cid.nome}</option>
    </g:each>
</select>
