<label for="estados">Estado</label>
<select name="estados" class="form-control selectpicker enable" multiple>
    <g:each in="${estadoInstanceList}" var="est">
        <option value="${est.id}">${est.nome}</option>
    </g:each>
</select>
