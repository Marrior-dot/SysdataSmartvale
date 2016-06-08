package com.acception.gestaofrota

class FormUtilTagLib {
    static namespace = "bs"
    static defaultEncodeAs = "raw"
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def formField = { attrs, body ->
        def value = attrs.value ?: ''
        def required = attrs.required ?: false
        def label = required ? "$attrs.label *" : attrs.label
        def id = attrs.id ?: '';
        def name = attrs.name ?: id
        def type = attrs.type ?: 'text'
        def clazz = attrs.class ?: ''
        def width = attrs.width ?: 100
        def disabled = attrs.disabled ?: false
        def hasfeedback = attrs.hasfeedback ?: true
        def inline = attrs.inline ?: false
        def maxlength = attrs.maxlength ?: null

        out << """
        <div class="form-group ${hasfeedback ? 'has-feedback' : ''}" ${inline ? 'style="display: inline-block"' : ''}>
            <label class="control-label" for="$id">$label</label>
            ${disabled ? "<input id='$id' name='$name' type='hidden' value='$value'>" : ''}
            ${inline ? '<div class="form-inline">' : ''}
            <input ${ id ? "id='${id}'" : '' } name="$name" type="$type" ${maxlength ? "maxlength=$maxlength" : ""} style="max-width: $width%" class="form-control $clazz"
                ${ type.trim().equalsIgnoreCase('cpf')  ? 'data-rule-cpf="true"' : '' }
                ${ type.trim().equalsIgnoreCase('cnpj') ? 'data-rule-cnpj="true"' : '' }
                value="$value" ${required ? 'required' : ''} ${attrs.size ? "size='${attrs.size}'" : ''} ${disabled ? 'disabled' : ''}>
            ${hasfeedback ? "<span class='glyphicon form-control-feedback' aria-hidden='true'></span>" : ""}
            ${inline ? '</div>' : ''}
        </div>
        """
    }

    def hiddenFields = { attrs, body ->
        def list = attrs.map
        if (list) {
            list.each {
                out << """ <input type="hidden" id="${it.key}" name="${it.key}" value="${it.value}"/> """
            }
        }
    }

}
