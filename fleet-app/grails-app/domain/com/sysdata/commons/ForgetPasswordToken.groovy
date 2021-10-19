package com.sysdata.commons

import com.sysdata.gestaofrota.User

class ForgetPasswordToken {

    Date dateCreated
    User user
    String newPasswordUrl
    Date expirationDatetime
    StatusToken statusToken = StatusToken.CREATED

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'fgtpwdtoken_seq']
    }
}
