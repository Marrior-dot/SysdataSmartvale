<%@ page import="com.sysdata.gestaofrota.Util" %>

<div class="panel panel-default">

    <div class="panel-heading">
        <g:if test="${!action || action == Util.ACTION_VIEW}">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                <g:form action="create">
                    <input type="hidden" name="empId" value="${empId}"/>
                    <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-plus"></i>&nbsp;Criar Estabelecimento</button>
                </g:form>
            </sec:ifAnyGranted>
        </g:if>
    </div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <th>Cod.Estab</th>
            <th>Razão Social</th>
            <th>Nome Fantasia</th>
            </thead>
            <tbody>
            <g:each in="${estabelementosList}" var="est">
                <tr>
                    <td><g:link action="show" id="${est.id}">${est.codigo}</g:link></td>
                    <td>${est.nome}</td>
                    <td>${est.nomeFantasia}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

</div>





