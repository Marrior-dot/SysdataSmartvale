package com.sysdata.gestaofrota

class UserTagLib {

    static namespace = "sys"

    def springSecurityService

    Closure propertyOwner = { attrs, body ->
        def user = springSecurityService.currentUser

        if (user.owner.instanceOf(attrs.ownerType)) {
            def assertion = user.owner."${attrs.property}" == "${attrs.value}"
            if (assertion)
                body()
        }

    }
}
