<style type="text/css">
    :root {
        --cor-primaria: ${projeto.corPrimaria};
        --cor-secundaria: ${projeto.corSecundaria};
    }
</style>

<title><g:layoutTitle default="${projeto?.nome}"/></title>
<link rel="shortcut icon" href="${resource(dir: 'images/projetos/' + projeto?.pasta, file: 'icon.png')}" type="image/x-icon"/>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap/bootstrap.min.css')}"/>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'cores.css')}"/>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'estilo.css')}"/>