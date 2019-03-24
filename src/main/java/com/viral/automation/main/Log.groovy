package com.viral.automation.main

class Log {

    private static final boolean DEBUG_FLAG = true;

    private static APPLICATION_TAG = '[ViralAutomation] '
    private static DEBUG_TAG = '[INFO] '
    private static INFO_TAG = '[INFO] '
    private static WARN_TAG = '[WARN] '
    private static ERROR_TAG = '[ERROR] '
    private static SUCCESS_TAG = '[SUCCESS] '

    private static SEPARATOR = "\n\n"

    static void info(String log) {println APPLICATION_TAG + INFO_TAG + log}
    static void warn(String log) {println APPLICATION_TAG + WARN_TAG + log}
    static void error(String log) {println APPLICATION_TAG + ERROR_TAG + log + SEPARATOR}
    static void success(String log) {println APPLICATION_TAG + SUCCESS_TAG + log + SEPARATOR}

    static void debug(String log) {
        if (DEBUG_FLAG == true) {
            println APPLICATION_TAG + DEBUG_TAG + log
        }
    }
}
