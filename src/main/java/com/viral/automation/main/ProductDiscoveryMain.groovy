package com.viral.automation.main

import com.viral.automation.authentication.ViralLogin
import com.viral.automation.productdiscovery.ViralProductDiscovery
import geb.Browser

class ProductDiscoveryMain {

    // This is an entry point for IntelliJ target only
    static void main(String... args) {

        final String EMAIL = args[0]
        final String PASSWORD = args[1]

        final String TERM = args[2]
        final String PRESETNAME = args[3]
        final String PAGESTOTRANSCRIBE = args[4]

        print executeProductDiscovery(EMAIL, PASSWORD, TERM, PRESETNAME, PAGESTOTRANSCRIBE)
    }

    // This is a method serving a different file (the endpoint for bash). In bash we return the result to be written to a file.
    static String executeProductDiscovery(String email, String password, String term, String presetName, String pagesToTranscribe) {
        List<String> productDiscoveryResults = new ArrayList<>()

        List<Browser> browsers = new ArrayList<>()
        browsers.add ViralLogin.launch(email, password)
        browsers.add ViralProductDiscovery.discoverAndRecord(term, presetName, pagesToTranscribe.toInteger(), productDiscoveryResults)
        browsers.each { it.quit() }

        return productDiscoveryResults
    }
}

