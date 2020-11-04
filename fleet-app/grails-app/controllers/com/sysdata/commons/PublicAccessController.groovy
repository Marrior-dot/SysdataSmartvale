package com.sysdata.commons

import com.sysdata.gestaofrota.User

class PublicAccessController {

    AccessKeyService accessKeyService

    def index() { }

    def forgotPassword() {
        accessKeyService
    }

    def changePassword() {
        User user = User.get(params.id.toLong())
        if (user) {

            def ret = accessKeyService.changePassword(user, params.password, params.confirmation)
            if (ret.success) {

            }
        }
    }
}
