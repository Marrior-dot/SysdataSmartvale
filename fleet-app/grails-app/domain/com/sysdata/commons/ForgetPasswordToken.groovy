package com.sysdata.commons

import com.sysdata.gestaofrota.User

class ForgetPasswordToken {

    Date dateCreated
    User user
    String key
    Date expirationDatetime
    StatusToken statusToken = StatusToken.CREATED

    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'fgtpwdtoken_seq']
    }
}
