package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceAnalyzer
import com.viral.automation.authentication.ViralLogin
import com.viral.automation.marketintelligence.ViralMarketIntelligence
import geb.Browser

class MarketIntelligenceMain {

    // This is an entry point for IntelliJ target only
    static void main(String... args) {

        final String EMAIL = args[0]
        final String PASSWORD = args[1]
        final String TERM = args[2]

        print executeMarketIntelligence(EMAIL, PASSWORD, TERM)
   }

    // This is a method serving a different file (the endpoint for bash). In bash we return the result to be written to a file.
    static LinkedHashMap executeMarketIntelligence(final String email, final String password, final String termOrFilename) {

        final List<String> terms = resolveInput(termOrFilename)

        def marketIntelligenceResults = [:]

        final List<Browser> browsers = new ArrayList<>()
        browsers.add ViralLogin.launch(email, password)
        for (String term : terms) {
            if (isValid(term)) {
                browsers.add ViralMarketIntelligence.searchAndRecord(term, marketIntelligenceResults)
            } else {
                addAsInvalidEntry(term, marketIntelligenceResults)
            }
        }
        browsers.each { it.quit() }

        final LinkedHashMap analysis = ViralMarketIntelligenceAnalyzer.analyzeProducts(marketIntelligenceResults)
        return analysis
    }

    static List<String> resolveInput(final String termOrFilename) {
        try {
            final File fileContainingTerms = new File(termOrFilename)
            final String rawTerms = fileContainingTerms.getText('UTF-8')
            return rawTerms.split('\n')
        } catch (FileNotFoundException e) {
            return Arrays.asList(termOrFilename)
        }
    }
}
