package com.sysdata.xfiles.writer


class SeparatorWriter extends CommonWriter {

    String separator

    @Override
    def writeField(Object rawFieldValue, Map specField) {
        this.buffer.append(rawFieldValue).append(this.separator)
    }
}