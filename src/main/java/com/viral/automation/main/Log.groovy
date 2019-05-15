package com.viral.automation.main

import org.codehaus.groovy.reflection.ReflectionUtils

class Log {

    private static final boolean DEBUG_FLAG = true

    private static DEBUG_TAG = '[DEBUG] '
    private static INFO_TAG = '[INFO] '
    private static WARN_TAG = '[WARN] '
    private static ERROR_TAG = '[ERROR] '
    private static SUCCESS_TAG = '[SUCCESS] '

    private static SEPARATOR = "\n\n"

    static void info(String log) {println getCallingClassTag() + INFO_TAG + log}
    static void warn(String log) {println getCallingClassTag() + WARN_TAG + log}
    static void error(String log) {println getCallingClassTag() + ERROR_TAG + log + SEPARATOR}
    static void success(String log) {println getCallingClassTag() + SUCCESS_TAG + log + SEPARATOR}

    static void debug(String log) {
        if (DEBUG_FLAG == true) {
            println getCallingClassTag() + DEBUG_TAG + log
        }
    }

    static String getCallingClassTag() {
        return "[" + ReflectionUtils.getCallingClass(2).simpleName + "] "
    }
}
