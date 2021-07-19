<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>
<g:if test="${flash.errors}">
    <div class="alert alert-danger">${flash.errors}</div>
</g:if>