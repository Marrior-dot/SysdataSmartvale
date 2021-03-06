<%@ page import="com.sysdata.gestaofrota.Unidade" %>
<div class="panel panel-default">
    <div class="panel-heading">Lista de Unidades</div>
    <div class="panel-body">
        <g:form controller="${controller}">
            <g:link controller="unidade" action="create" class="btn btn-default" params="[rhId: rhId]">
                <span class="glyphicon glyphicon-plus"></span>&nbsp;Nova Unidade
            </g:link>

            <div class="panel panel-default panel-top">
                <div class="panel-body">
                    <table class="table table-striped table-bordered table-hover table-condensed table-default" >
                    <thead>
                        <th>Código</th>
                        <th>Nome</th>
                        <th>Status</th>
                        <th></th>
                    </thead>
                    <tbody>
                        <%
                            def unidadeList = Unidade.withCriteria {
                                                        rh {
                                                            eq('id',rhId)
                                                        }
                                                        order("dateCreated")
                                            }
                        %>

                        <g:each in="${unidadeList}" var="unidade" >
                            <tr>
                                <td><g:link controller="unidade" action="show" id="${unidade.id}">${unidade?.codigo}</g:link></td>
                                <td>${unidade?.nome}</td>
                                <td>${unidade?.status}</td>
                                <td></td>
                            </tr>
                        </g:each>
                    </tbody>
                    </table>
                </div>
            </div>
        </g:form>
    </div>
</div>







