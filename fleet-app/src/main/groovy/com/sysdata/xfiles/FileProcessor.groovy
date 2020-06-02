package com.sysdata.xfiles

abstract class FileProcessor {


    static void init() {
        WriterProcessor.initProcessor()
        ReaderProcessor.initProcessor()
    }

}