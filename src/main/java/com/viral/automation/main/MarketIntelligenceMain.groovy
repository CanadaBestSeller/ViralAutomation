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
    static String executeMarketIntelligence(String EMAIL, String PASSWORD, String TERM) {

        def marketIntelligenceResults = [:]

        final List<Browser> browsers = new ArrayList<>()
        browsers.add ViralLogin.launch(EMAIL, PASSWORD)
        browsers.add ViralMarketIntelligence.searchAndRecord(TERM, marketIntelligenceResults)
        browsers.each { it.quit() }

        return ViralMarketIntelligenceAnalyzer.analyzeProducts(marketIntelligenceResults)
    }
}
