package com.sysdata.xfiles

enum LineFeed {

    WIN("Windows", "\r\n"),
    LINUX("Linux", "\n"),
    MAC("Mac", "\r")

    String name
    String chars

    LineFeed(name, chars) {
        this.name = name
        this.chars = chars
    }
}

