package com.viral.automation

class Log {
    private static APPLICATION_TAG = '[ViralAutomation] '
    private static INFO_TAG = '[INFO] '
    private static WARN_TAG = '[WARN] '
    private static ERROR_TAG = '[ERROR] '
    private static SUCCESS_TAG = '[SUCCESS] '

    private static SEPARATOR = "\n\n\n\n"

    static void info(String log) {println APPLICATION_TAG + INFO_TAG + log}
    static void warn(String log) {println APPLICATION_TAG + WARN_TAG + log}
    static void error(String log) {println APPLICATION_TAG + ERROR_TAG + log + SEPARATOR}
    static void success(String log) {println APPLICATION_TAG + SUCCESS_TAG + log + SEPARATOR}
}
