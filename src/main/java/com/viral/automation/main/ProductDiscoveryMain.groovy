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
        final String PAGES_TO_TRANSCRIBE = args[3]

        print executeProductDiscovery(EMAIL, PASSWORD, TERM, PAGES_TO_TRANSCRIBE)
    }

    // This is a method serving a different file (the endpoint for bash). In bash we return the result to be written to a file.
    static Set<String> executeProductDiscovery(
            final String email,
            final String password,
            final String termOrFilename,
            final String pagesToTranscribe
    ) {
        final Set<String> terms = resolveInput(termOrFilename)

        final Set<String> productDiscoveryResults = new HashSet<>()

        final List<Browser> browsers = new ArrayList<>()
        browsers.add ViralLogin.launch(email, password)
        for (String term : terms) {
            browsers.add ViralProductDiscovery.discoverAndRecord(term, pagesToTranscribe.toInteger(), productDiscoveryResults)
        }
        browsers.each { it.quit() }
        return productDiscoveryResults
    }

    static Set<String> resolveInput(final String termOrFilename) {
        try {
            final File fileContainingTerms = new File(termOrFilename)
            final String rawTerms = fileContainingTerms.getText('UTF-8')
            return rawTerms.split('\n') as Set
        } catch (FileNotFoundException e) {
            return Arrays.asList(termOrFilename)
        }
    }
}
