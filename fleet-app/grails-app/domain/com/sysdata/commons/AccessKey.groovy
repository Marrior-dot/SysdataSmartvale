package com.sysdata.commons

import com.sysdata.gestaofrota.User

class AccessKey {

    Date dateCreated
    String key
    Date expirationDate
    StatusAccessKey status = StatusAccessKey.VALID
    User user

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "accesskey_seq"]
    }
}
